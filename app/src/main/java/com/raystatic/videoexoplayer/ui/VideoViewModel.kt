package com.raystatic.videoexoplayer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raystatic.videoexoplayer.data.model.VideoResponseItem
import com.raystatic.videoexoplayer.repositories.VideoRepositories
import com.raystatic.videoexoplayer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepositories: VideoRepositories
):ViewModel(){

    private val _videos = MutableLiveData<Resource<List<VideoResponseItem>>>()
    val videos:LiveData<Resource<List<VideoResponseItem>>> get() = _videos

    fun getVideos() = viewModelScope.launch {
        _videos.postValue(Resource.loading())
        try {
            videoRepositories.fetchVideos().also {
                if (it.isSuccessful){
                    _videos.postValue(Resource.success(it.body()))
                }else{
                    _videos.postValue(Resource.error("Error"))
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            _videos.postValue(Resource.error("Error"))
        }
    }

}