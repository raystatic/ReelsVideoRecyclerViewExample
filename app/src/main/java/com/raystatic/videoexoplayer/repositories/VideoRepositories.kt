package com.raystatic.videoexoplayer.repositories

import androidx.lifecycle.LiveData
import com.raystatic.videoexoplayer.data.datasources.RemoteDataSource
import com.raystatic.videoexoplayer.data.model.VideoResponseItem
import com.raystatic.videoexoplayer.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class VideoRepositories @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun fetchVideos():Response<List<VideoResponseItem>>{
        return remoteDataSource.fetchVideos(10)
    }

}