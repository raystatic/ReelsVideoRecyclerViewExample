package com.raystatic.videoexoplayer

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.media2.exoplayer.external.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util


const val TAG= "VIDEODEBUG"
class PlayerViewAdapter {


    companion object{
        private var playersMap: MutableMap<Int,SimpleExoPlayer> = mutableMapOf()
        private var currentPlayingVideo:Pair<Int,SimpleExoPlayer>?=null

        fun releaseAllPlayers(){
            playersMap.map {
                it.value.release()
            }
        }

        fun releaseRecycledPlayers(index:Int){
            playersMap[index]?.release()
        }

        fun pauseCurrentPlayingVideo(){
            if (currentPlayingVideo !=null){
                currentPlayingVideo?.second?.playWhenReady = false
            }
        }

        fun playIndexThenPausePreviousPlayer(index: Int){
            if (playersMap[index]?.playWhenReady == false){
                pauseCurrentPlayingVideo()
                playersMap[index]?.playWhenReady = true
                currentPlayingVideo = Pair(index, playersMap[index]!!)
            }
        }

        fun PlayerView.loadVideo(
            context: Context,
            url:String,
            callback: PlayerStateCallback,
            itemIndex:Int?=null,
            autoPlay:Boolean = false
        ){
            val player = SimpleExoPlayer.Builder(context).build()

            player.playWhenReady = autoPlay
            player.repeatMode = Player.REPEAT_MODE_ALL

            Log.d(TAG, "VIDEODEBUG: a $player")

            setKeepContentOnPlayerReset(true)

            this.useController = false

            val dataSourceFactory = DefaultHttpDataSource.Factory()

            Log.d(TAG, "VIDEODEBUG: b $dataSourceFactory")

            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(url))

            Log.d(TAG, "VIDEODEBUG: c $mediaSource")

            player.setMediaSource(mediaSource)

            Log.d(TAG, "VIDEODEBUG: d $mediaSource")

            this.player = player

            Log.d(TAG, "VIDEODEBUG: e $player ${this.player}")

            if (playersMap.containsKey(itemIndex)){
                Log.d(TAG, "VIDEODEBUG: f $itemIndex")
                playersMap.remove(itemIndex)
            }

            if (itemIndex != null){
                playersMap[itemIndex] = player
                Log.d(TAG, "VIDEODEBUG: g $player ${playersMap[itemIndex]}")
            }


            this.player!!.addListener(object : Player.Listener {
                override fun onPlayerError(error: ExoPlaybackException) {
                    super.onPlayerError(error)
                    Log.d(TAG, "VIDEODEBUG: onPlayerError $error")
                    callback.onError(error = error.localizedMessage.toString())
                }

                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    super.onPlayWhenReadyChanged(playWhenReady, reason)
                    println("VIDEODEBUG: onPlayWhenReadyChanged  $playWhenReady $reason")
                    if (reason == Player.STATE_BUFFERING) {
                        callback.onVideoBuffering(player)
                    }

                    if (reason == Player.STATE_READY) {
                        callback.onVideoDurationRetrieved(this@loadVideo.player!!.duration, player)
                    }

                    if (reason == Player.STATE_READY && player.playWhenReady) {
                        callback.onStartedPlaying(player)
                    }

                    if (reason == Player.STATE_ENDED) {
                        callback.onFinishedPlaying(player)
                    }

                }
            })

           // player.prepare()

        }

    }

}