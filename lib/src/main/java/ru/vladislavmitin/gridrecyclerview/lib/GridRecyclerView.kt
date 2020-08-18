package ru.vladislavmitin.gridrecyclerview.lib

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

open class GridRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    companion object {
        private const val DEFAULT_ROW_COUNT = 1
        private const val DEFAULT_COLUMN_COUNT = 1
    }

    var rows = DEFAULT_ROW_COUNT
        set(value) {
            field = value
            initAdapter()
        }

    var columns = DEFAULT_COLUMN_COUNT
        set(value) {
            field = value
            initAdapter()
        }

    private var childWidth = 0
    private var childHeight = 0
    private var childLayoutParams: LayoutParams

    private var adapter: Adapter<*>? = null
    private var currentPage = 1

    init {
        val obtainedAttrs = context.obtainStyledAttributes(
            attrs,
            R.styleable.GridRecyclerView, 0, 0
        )

        rows = obtainedAttrs.getInteger(R.styleable.GridRecyclerView_rows, 0)
        columns = obtainedAttrs.getInteger(R.styleable.GridRecyclerView_columns, 0)

        obtainedAttrs.recycle()

        childLayoutParams = LayoutParams(0,0)
    }

    //region Override

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        childWidth = calculateChildWidth(width)
        childHeight = calculateChildHeight(height)

        val childWidthSpec = getChildMeasureSpec(
            widthMeasureSpec, 0, childWidth
        )

        val childHeightSpec = getChildMeasureSpec(
            heightMeasureSpec, 0, childHeight
        )

        adapter?.getViews()?.forEach { view -> view.measure(childWidthSpec, childHeightSpec) }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        adapter?.let { ad ->
            layoutChildren(
                if (childCount == 0) ad.getViews() else children.toList(),
                childCount == 0
            )

            bindViews()
        }
    }

    //endregion

    //region Public Methods

    fun nextPage(): Boolean {
        return if(currentPage * rows * columns < adapter?.getItemCount() ?: 0) {
            currentPage++
            bindViews()
            true
        } else false
    }

    fun prevPage(): Boolean {
        return if (currentPage > 1) {
            currentPage--
            bindViews()
            true
        } else false
    }

    fun <VH: ViewHolder> setAdapter(adapter: Adapter<VH>) {
        this.adapter = adapter
        initAdapter()
    }

    fun getAdapter() = adapter

    //endregion

    //region Private Methods

    private fun layoutChildren(
        children: List<View>,
        attachToParent: Boolean
    ) {
        val childLeft = this.paddingLeft
        val childTop = this.paddingTop

        var currentLeft = childLeft
        var currentTop = childTop

        var index = 0

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val child = children[index++]

                if(attachToParent) {
                    addViewInLayout(child, -1, getChildLayoutParams())
                }

                child.layout(
                    currentLeft,
                    currentTop,
                    currentLeft + childWidth,
                    currentTop + childHeight
                )

                currentLeft += childWidth
            }

            currentLeft = 0
            currentTop += childHeight
        }
    }

    private fun getChildLayoutParams() = childLayoutParams.apply {
        this.width = childWidth
        this.height = childHeight
    }

    private fun bindViews() {
        adapter?.let {  ad ->
            val from = (currentPage - 1) * rows * columns
            val count = if (currentPage * rows * columns < ad.getItemCount()) rows * columns else ad.getItemCount() - from

            ad.bind(from, count)
        }
    }

    private fun calculateChildHeight(parentHeight: Int) = (parentHeight - paddingTop - paddingBottom) / rows
    private fun calculateChildWidth(parentWidth: Int) = (parentWidth - paddingLeft - paddingRight) / columns

    private fun initAdapter() {
        adapter?.let {
            it.initChildren(this, rows*columns)
            requestLayout()
        }
    }

    //endregion

    abstract class Adapter<VH: ViewHolder> {
        private val holders = ArrayList<VH>()

        abstract fun onCreateViewHolder(parent: ViewGroup): VH
        abstract fun onBindViewHolder(holder: VH, position: Int)
        abstract fun getItemCount(): Int

        private fun createViewHolder(parent: ViewGroup): VH {
            return onCreateViewHolder(parent)
        }

        fun initChildren(parent: ViewGroup, count: Int) {
            parent.removeAllViews()
            holders.clear()

            for(i in 0 until count) {
                holders.add(createViewHolder(parent))
            }
        }

        fun bind(positionFrom: Int, count: Int) {
            var holderIndex = 0

            for (i in positionFrom until positionFrom + count) {
                holders[holderIndex].itemView.visibility = View.VISIBLE
                onBindViewHolder(holders[holderIndex], i)
                holderIndex++
            }

            if(holderIndex <= (holders.size - 1)) {
                for (i in holderIndex until holders.size) {
                    holders[i].itemView.visibility = View.GONE
                }
            }
        }

        fun getViews() = holders.map { it.itemView }
    }

    abstract class ViewHolder(val itemView: View)
}
