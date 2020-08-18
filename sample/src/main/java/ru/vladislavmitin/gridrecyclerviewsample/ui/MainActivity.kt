package ru.vladislavmitin.gridrecyclerviewsample.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vladislavmitin.gridrecyclerviewsample.R
import ru.vladislavmitin.gridrecyclerviewsample.data.api.IService

class MainActivity: AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(IService::class.java)

        viewModel = ViewModelProvider(this, MainViewModelFactory(service)).get(MainViewModel::class.java)

        viewModel.photos.observe(this, Observer { photos ->
            activityMainGrid.setAdapter(Adapter(this, photos))
        })

        viewModel.loading.observe(this, Observer {
            activityMainProgressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(this, Observer {
            Snackbar.make(activityMainGrid, it, Snackbar.LENGTH_INDEFINITE).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuMainPrevPage -> {
                activityMainGrid.prevPage()
                true
            }
            R.id.menuMainNextPage -> {
                activityMainGrid.nextPage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}