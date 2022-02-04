package com.raystatic.videoexoplayer.repositories

import androidx.lifecycle.LiveData
import com.raystatic.videoexoplayer.data.ApiService
import com.raystatic.videoexoplayer.data.datasources.RemoteDataSource
import com.raystatic.videoexoplayer.data.model.VideoResponseItem
import com.raystatic.videoexoplayer.data.responses.Video
import com.raystatic.videoexoplayer.data.responses.VideosResponse
import com.raystatic.videoexoplayer.data.responses.pixabay.Hit
import com.raystatic.videoexoplayer.util.DataState
import com.raystatic.videoexoplayer.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class VideoRepositories @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getVideos():Flow<DataState<List<Hit>>> = flow {
        emit(DataState.Loading)
        try {
            val videoResponse = apiService.getPixabayVideos(token = "22697065-f755ae7e13f667111bfc5bc6a")
//            val videoResponse = apiService.getTrendingVideos(token = "563492ad6f917000010000014b1c00a1326e4116a695d1d2318a91be")
            emit(DataState.Success(videoResponse.hits))
        }catch (e:Exception){
            e.printStackTrace()
            emit(DataState.Error(e))
        }
    }

}