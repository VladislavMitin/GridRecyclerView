package ru.vladislavmitin.gridrecyclerviewsample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.vladislavmitin.gridrecyclerviewsample.data.api.IService

class MainViewModelFactory(private val service: IService): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(service) as T
    }
}