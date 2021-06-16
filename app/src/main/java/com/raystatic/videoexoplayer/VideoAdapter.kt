package com.raystatic.videoexoplayer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.android.exoplayer2.Player
import com.raystatic.videoexoplayer.PlayerViewAdapter.Companion.loadVideo
import com.raystatic.videoexoplayer.databinding.ItemVideoBinding

class VideoAdapter(
    private val context: Context,
    private val list: List<MediaObject>,
    private val onItemClick:(View,Int,MediaObject) -> Unit
):RecyclerView.Adapter<VideoAdapter.VideoPlayerViewHolder>(), PlayerStateCallback {

    private val TAG = "VIDEODEBUG"

    class VideoPlayerViewHolder(private val binding:ItemVideoBinding):RecyclerView.ViewHolder(binding.root){

        val thumnbail = binding.thumbnail
        val frameLayout = binding.mediaContainer
        val title = binding.title
        val volumeControl = binding.volumeControl
        val progressBar = binding.progressBar
        val parent = binding.root


        fun bind(model:MediaObject, position:Int){
            Log.d(TAG, "bind: $model")
            binding.apply {
                parent.tag = this@VideoPlayerViewHolder
                title.text = model.title
                Glide.with(itemView)
                    .load(model.thumbnail)
                    .into(thumbnail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPlayerViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoPlayerViewHolder, position: Int) {
        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {
        Log.d(TAG, "onVideoDurationRetrieved: $duration $player")
    }

    override fun onVideoBuffering(player: Player) {
        Log.d(TAG, "onVideoBuffering: $player")
    }

    override fun onStartedPlaying(player: Player) {
        Log.d(TAG, "onStartedPlaying: $player")
    }

    override fun onFinishedPlaying(player: Player) {
        Log.d(TAG, "onFinishedPlaying: $player")
    }

    override fun onError(error: String) {
        Log.d(TAG, "onError: $error")
    }
}