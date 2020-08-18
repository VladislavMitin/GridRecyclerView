package ru.vladislavmitin.gridrecyclerviewsample.data.api

import com.google.gson.annotations.SerializedName

class PhotoDto {
    @SerializedName("urls")
    var url: UrlDto? = null
}

class UrlDto {
    @SerializedName("raw")
    var raw: String? = null
    @SerializedName("full")
    var full: String? = null
    @SerializedName("regular")
    var regular: String? = null
    @SerializedName("small")
    var small: String? = null
    @SerializedName("thumb")
    var thumb: String? = null
}