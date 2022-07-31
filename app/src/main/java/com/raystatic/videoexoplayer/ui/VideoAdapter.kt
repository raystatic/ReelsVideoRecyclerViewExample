package com.raystatic.videoexoplayer.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raystatic.videoexoplayer.data.model.Video
import com.raystatic.videoexoplayer.data.model.VideoResponseItem
import com.raystatic.videoexoplayer.data.responses.pixabay.Hit
import com.raystatic.videoexoplayer.databinding.ItemVideoBinding

class VideoAdapter:RecyclerView.Adapter<VideoAdapter.VideoPlayerViewHolder>() {

    private val TAG = "VIDEODEBUG"

    private val diffCallback = object : DiffUtil.ItemCallback<Hit>(){
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Hit,
            newItem: Hit
        ) = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this,diffCallback)

    fun submitData(list: List<Hit>) = differ.submitList(list)

//    fun submitData(l:List<Hit>){
//        list = l
//        notifyDataSetChanged()
//    }

    class VideoPlayerViewHolder(private val binding:ItemVideoBinding):RecyclerView.ViewHolder(binding.root){

        val thumnbail = binding.thumbnail
        val frameLayout = binding.mediaContainer
        val title = binding.title
        val volumeControl = binding.volumeControl
        val progressBar = binding.progressBar
        val parent = binding.root

        fun bind(model: Hit, position:Int){
            println("VIDEODEBUG: model: $model")
            binding.apply {
                parent.tag = this@VideoPlayerViewHolder
                Glide.with(itemView)
                    .load(model.userImageURL)
                    .into(thumbnail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPlayerViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoPlayerViewHolder, position: Int) {
        holder.bind(differ.currentList[position],position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}