package com.raystatic.videoexoplayer.util

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.raystatic.videoexoplayer.R
import com.raystatic.videoexoplayer.data.model.Video
import com.raystatic.videoexoplayer.data.model.VideoResponseItem
import com.raystatic.videoexoplayer.data.responses.pixabay.Hit
import com.raystatic.videoexoplayer.ui.VideoAdapter

class CustomVideoRecyclerView: RecyclerView{


    companion object {
        const val TAG ="VIDEODEBUG"
    }

    private enum class VolumeState {ON,OFF}

    // ui
    private var thumbnail: ImageView? = null
    private  var volumeControl:ImageView? = null
    private var progressBar: ProgressBar? = null
    private var viewHolderParent: View? = null
    private var frameLayout: FrameLayout? = null
    lateinit var videoSurfaceView:PlayerView
    private var videoPlayer:SimpleExoPlayer?=null

    //variables
    private var mediaObjects = mutableListOf<Hit>()
    private var videoSurfaceDefaultHeight = 0
    private var screenDefaultHeight = 0
    private lateinit var ctx:Context
    private var playPosition = -1
    private var isVideoViewAdded = false

    // playback state
    private lateinit var volumeState: VolumeState

    constructor(context: Context):super(context){
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet):super(context, attributeSet){
        init(context)
    }

    private fun init(context: Context){
        ctx = context.applicationContext
        val display = (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val point = Point()
        display?.getSize(point)
        videoSurfaceDefaultHeight = point.x
        screenDefaultHeight = point.y

        videoSurfaceView = PlayerView(this.ctx)
        videoSurfaceView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        // create the player
        videoPlayer = SimpleExoPlayer.Builder(context).build()

        // bind player to view
        videoSurfaceView.useController = true
        videoSurfaceView.player = videoPlayer
        setVolumeControl(VolumeState.ON)

        addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged: called")
                    thumbnail?.isVisible = true

                    // There's a special case when the end of the list has been reached.
                    // Need to handle that with this bit of logic
                    if (!recyclerView.canScrollVertically(1)) {
                        playVideo(true)
                    } else {
                        playVideo(false)
                    }

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {

            }

            override fun onChildViewDetachedFromWindow(view: View) {
                if (viewHolderParent != null && viewHolderParent!! == view) {
                    resetVideoView()
                }
            }
        })

        videoPlayer?.addListener(object : Player.Listener {
            override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {

            }

            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_BUFFERING -> {
                        Log.d(TAG, "onPlaybackStateChanged: buffering")
                        progressBar?.isVisible = true
                    }

                    Player.STATE_ENDED -> {
                        Log.d(TAG, "onPlaybackStateChanged: ended")
                        videoPlayer?.seekTo(0)
                    }

                    Player.STATE_IDLE -> {
                        Log.d(TAG, "onPlaybackStateChanged: idle")
                    }

                    Player.STATE_READY -> {
                        Log.d(TAG, "onPlaybackStateChanged: ready")
                        progressBar?.isVisible = false
                        if (!isVideoViewAdded) {
                            addVideoView()
                        }
                    }
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {

            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

            }

            override fun onPlayerError(error: ExoPlaybackException) {

            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

            }

        })

    }

    fun releasePlayer(){
        if (videoPlayer != null){
            videoPlayer?.release()
            videoPlayer = null
        }
        viewHolderParent = null
    }

    private fun addVideoView() {
        frameLayout?.addView(videoSurfaceView)
        isVideoViewAdded = true
        videoSurfaceView.requestFocus()
        videoSurfaceView.isVisible = true
        videoSurfaceView.alpha = 1f
        thumbnail?.isVisible = false
    }

    private fun resetVideoView() {
        if (isVideoViewAdded){
            removeVideoView(videoSurfaceView)
            playPosition = -1
            videoSurfaceView.visibility = View.INVISIBLE
            thumbnail?.isVisible = true
        }
    }

    private fun playVideo(isEndOfList: Boolean) {
        var targetPosition = -1

        if (!isEndOfList){
            var startPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            var endPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1){
                endPosition = startPosition + 1
            }

            // something is wrong. return

            if (startPosition < 0 || endPosition < 0){
                return
            }

            // if there is more than 1 list-item on the screen
            if (startPosition != endPosition){
                val startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition)
                val endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition)

                targetPosition = if (startPositionVideoHeight > endPositionVideoHeight)
                                        startPosition
                                else
                                    endPosition
            }else{
                targetPosition = startPosition
            }
        }else{
            targetPosition = mediaObjects.size -1
        }

        Log.d(TAG, "playVideo: targetPosition: $targetPosition")

        // video is already playing so return
        if (targetPosition == playPosition){
            return
        }

        // set the position of the list-item is to be played
        playPosition = targetPosition

        if (!this::videoSurfaceView.isInitialized){
            return
        }

        // remove any old surface views from previously playing videos
        videoSurfaceView.visibility = View.INVISIBLE
        println("$TAG videoSurfaceView: $videoSurfaceView")
        removeVideoView(videoSurfaceView)

        val currentPosition = targetPosition - (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        val child = getChildAt(currentPosition) ?: return

        val holder = child.tag as VideoAdapter.VideoPlayerViewHolder

        if (holder == null){
            playPosition  = -1
            return
        }

        thumbnail = holder.thumnbail
        progressBar = holder.progressBar
        volumeControl = holder.volumeControl
        viewHolderParent = holder.itemView
        frameLayout = holder.itemView.findViewById(R.id.media_container)

        videoSurfaceView.player = videoPlayer

        viewHolderParent!!.setOnClickListener {
            toggleVolume()
        }

        val dataSourceFactory = DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, "VideoRecyclerView")
        )

        val mediaUrl = mediaObjects[targetPosition].videos.medium.url
        Log.d(TAG, "playVideo: playaddr: $mediaUrl")
        if (mediaUrl != null){
            val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(mediaUrl))
            videoPlayer?.setMediaSource(videoSource)
            videoPlayer?.prepare()
            videoPlayer?.playWhenReady = true
        }


    }

    private fun toggleVolume() {
        if (videoPlayer != null){
            if (volumeState == VolumeState.OFF){
                Log.d(TAG, "toggleVolume: enabling volume")
                setVolumeControl(VolumeState.ON)
            }else if (volumeState == VolumeState.ON){
                Log.d(TAG, "toggleVolume: disabling volume")
                setVolumeControl(VolumeState.OFF)
            }
        }
    }

    private fun removeVideoView(videoView: PlayerView) {
        val parent = if (videoView.parent != null) videoView.parent  as ViewGroup else return

        val index = parent.indexOfChild(videoView)

        if (index >= 0){
            parent.removeViewAt(index)
            isVideoViewAdded = false
            viewHolderParent?.setOnClickListener(null)
        }
    }

    /**
     * Returns the visible region of the video surface on the screen.
     * if some is cut off, it will return less than the @videoSurfaceDefaultHeight
     * @param playPosition
     * @return
     */
    private fun getVisibleVideoSurfaceHeight(playPosition: Int): Int {
        val at = playPosition - (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: $at")

        val child = getChildAt(at) ?: return 0

        val location = IntArray(2)
        child.getLocationInWindow(location)

        if (location[1]<0){
            return location[1] + videoSurfaceDefaultHeight
        }else{
            return screenDefaultHeight - location[1]
        }

    }

    private fun setVolumeControl(state: VolumeState){
        volumeState = state
        if (state == VolumeState.OFF){
            videoPlayer?.volume = 0f
        }else if (state == VolumeState.ON){
            videoPlayer?.volume = 1f
        }
        animateVolumeControl()
    }

    private fun animateVolumeControl() {
        volumeControl?.apply {
            bringToFront()
            if (volumeState == VolumeState.OFF){
                Glide.with(context).load(R.drawable.ic_volume_off).into(this)
            }else if (volumeState == VolumeState.ON){
                Glide.with(context).load(R.drawable.ic_volume_up).into(this)
            }
            animate().cancel()
            alpha = 1f
            animate().alpha(0f).setDuration(600).startDelay = 1000
        }
    }

    fun setMediaObjects(mediaObjects: MutableList<Hit>){
        this.mediaObjects = mediaObjects
    }

}