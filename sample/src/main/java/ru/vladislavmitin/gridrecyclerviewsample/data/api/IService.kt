package ru.vladislavmitin.gridrecyclerviewsample.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface IService {
    @Headers("Accept-Version: v1")
    @GET("photos/random")
    suspend fun getRandomPhotoList(@Header("Authorization") authorization: String, @Query("count") count: Int): List<PhotoDto>
}