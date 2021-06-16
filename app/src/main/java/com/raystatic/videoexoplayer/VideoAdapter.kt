package com.raystatic.videoexoplayer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player
import com.raystatic.videoexoplayer.PlayerViewAdapter.Companion.loadVideo
import com.raystatic.videoexoplayer.databinding.ItemVideoBinding

class VideoAdapter(
    private val context: Context,
    private val list: List<MediaObject>,
    private val onItemClick:(View,Int,MediaObject) -> Unit
):RecyclerView.Adapter<RecyclerView.ViewHolder>(), PlayerStateCallback {

    private val TAG = "VIDEODEBUG"

    inner class VideoPlayerViewHolder(private val binding:ItemVideoBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(model:MediaObject, position:Int){
            Log.d(TAG, "bind: $model")
            binding.apply {
                itemVideoExoplayer.loadVideo(
                    context,
                    model.mediaUrl.toString(),
            this@VideoAdapter,
                    position,true
                )

                root.setOnClickListener {
                    onItemClick(it,position, model)
                }

            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
        PlayerViewAdapter.releaseRecycledPlayers(position)
        super.onViewRecycled(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(context),parent, false)
        return VideoPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is VideoPlayerViewHolder){
            holder.bind(model,position)
        }
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