package com.raystatic.videoexoplayer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raystatic.videoexoplayer.data.model.VideoResponseItem
import com.raystatic.videoexoplayer.data.responses.Video
import com.raystatic.videoexoplayer.data.responses.VideosResponse
import com.raystatic.videoexoplayer.data.responses.pixabay.Hit
import com.raystatic.videoexoplayer.repositories.VideoRepositories
import com.raystatic.videoexoplayer.util.DataState
import com.raystatic.videoexoplayer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepositories: VideoRepositories
):ViewModel(){

    private val _videos = MutableLiveData<DataState<List<Hit>>>()
    val videos:LiveData<DataState<List<Hit>>> get() = _videos


    fun setStateEvent(videoStateEvent: VideoStateEvent){
        viewModelScope.launch {
            when(videoStateEvent){
                is VideoStateEvent.GetVideos -> {
                    videoRepositories.getVideos()
                        .onEach {
                            _videos.value = it
                        }
                        .launchIn(viewModelScope)
                }

                VideoStateEvent.None -> {

                }
            }
        }
    }

}

sealed class VideoStateEvent{

    object GetVideos: VideoStateEvent()

    object None: VideoStateEvent()

}