package com.raystatic.videoexoplayer.data.model

data class Music(
    val album: String,
    val authorName: String,
    val coverLarge: String,
    val coverMedium: String,
    val coverThumb: String,
    val duration: Int,
    val id: String,
    val original: Boolean,
    val playUrl: String,
    val title: String
)