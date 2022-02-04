package com.raystatic.videoexoplayer.data.datasources

import android.util.Log
import com.raystatic.videoexoplayer.data.ApiService
import com.raystatic.videoexoplayer.data.model.VideoResponseItem
import com.raystatic.videoexoplayer.util.Resource
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

//    suspend fun fetchVideos(count:Int):Response<List<VideoResponseItem>>{
//        return apiService.getTrendingVideos(region = "IN",language = "en-US",count = count,apiKey = "O5pjwyWJWthaBYmWtu52a3OGgjfmNUkT")
//    }


    private suspend fun <T> getResponse(request: suspend () -> Response<T>, errorMessage:String): Resource<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful){
                return Resource.success(result.body())
            }else{
                Log.d("RESPONSE_ERROR:","${result.errorBody()}")
                Resource.error(errorMessage)
            }
        }catch (e:Exception){
            e.printStackTrace()
            return Resource.error("Something went wrong")
        }
    }

}