package com.raystatic.videoexoplayer.data

import com.raystatic.videoexoplayer.data.model.VideoResponseItem
import com.raystatic.videoexoplayer.data.responses.PexelsResponse
import com.raystatic.videoexoplayer.data.responses.VideosResponse
import com.raystatic.videoexoplayer.data.responses.pixabay.PixabayVideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("videos/popular")
    suspend fun getTrendingVideos(
            @Header("Authorization") token:String
    ):PexelsResponse

    @GET("videos/")
    suspend fun getPixabayVideos(
        @Query("key") token:String,
        @Query("q") query:String = "yellow+flowers",
    ):PixabayVideosResponse

}