package com.raystatic.videoexoplayer.data

import com.raystatic.videoexoplayer.data.model.VideoResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("video/trending")
    suspend fun getTrendingVideos(
            @Header("apiKey") apiKey:String,
            @Query("region") region:String,
            @Query("lang") language:String,
            @Query("count") count:Int
    ):Response<List<VideoResponseItem>>

}