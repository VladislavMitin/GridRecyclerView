package ru.vladislavmitin.gridrecyclerviewsample.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ru.vladislavmitin.gridrecyclerviewsample.BuildConfig
import ru.vladislavmitin.gridrecyclerviewsample.data.api.IService
import ru.vladislavmitin.gridrecyclerviewsample.data.api.PhotoDto

class MainViewModel(private val service: IService): ViewModel() {
    private val authorization = "Client-ID " + BuildConfig.API_KEY

    var loading = MutableLiveData<Boolean>(false)
    var error = MutableLiveData<String>()

    var photos = liveData {
        try {
            loading.postValue(true)
            val photos = service.getRandomPhotoList(authorization, 30)
            emit(photos.map { Photo.fromDto(it) }.toList())
        } catch (ex: Exception) {
            error.postValue(ex.message)
        } finally {
            loading.postValue(false)
        }
    }

    class Photo(val url: String) {
        companion object {
            fun fromDto(dto: PhotoDto): Photo? {
                val url = dto.url?.thumb ?: dto.url?.small ?: dto.url?.regular ?: dto.url?.full
                ?: dto.url?.raw

                url?.let {
                    return Photo(it)
                }

                return null
            }
        }
    }
}