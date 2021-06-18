package com.raystatic.videoexoplayer.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.raystatic.videoexoplayer.util.MediaObject
import com.raystatic.videoexoplayer.data.model.VideoResponseItem
import com.raystatic.videoexoplayer.databinding.ActivityMainBinding
import com.raystatic.videoexoplayer.util.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var videoAdapter: VideoAdapter

    private val vm by viewModels<VideoViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoAdapter = VideoAdapter()

        val snapHelper = LinearSnapHelper()

        binding.rvVideos.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = videoAdapter
            snapHelper.attachToRecyclerView(this)
        }

        vm.videos.observe(this, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let {
                        videoAdapter.submitData(it)
                        binding.rvVideos.setMediaObjects(it as MutableList<VideoResponseItem>)
                        binding.progressBar.isVisible = false
                    }
                }
                Resource.Status.LOADING -> {
                    binding.progressBar.isVisible = true
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(this, "err", Toast.LENGTH_SHORT).show()
                }
            }
        })

        vm.getVideos()

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.rvVideos.releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            binding.rvVideos.releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            binding.rvVideos.releasePlayer()
        }
    }

}