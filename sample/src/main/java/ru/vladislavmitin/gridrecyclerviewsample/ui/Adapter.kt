package ru.vladislavmitin.gridrecyclerviewsample.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import ru.vladislavmitin.gridrecyclerview.lib.GridRecyclerView
import ru.vladislavmitin.gridrecyclerviewsample.R

class Adapter(private val context: Context, private val items: List<MainViewModel.Photo?>): GridRecyclerView.Adapter<Adapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): ImageViewHolder {
        return ImageViewHolder(context, LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ImageViewHolder(private val context: Context, itemView: View): GridRecyclerView.ViewHolder(itemView) {
        fun bind(photo: MainViewModel.Photo?) {
            photo?.let {
                Glide.with(context)
                    .load(photo.url)
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .into(itemView as AppCompatImageView)
            }
        }
    }
}