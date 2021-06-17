package com.raystatic.videoexoplayer.data.model

data class Video(
    val bitrate: Int,
    val cover: String,
    val downloadAddr: String,
    val duration: Int,
    val dynamicCover: String,
    val encodeUserTag: String,
    val encodedType: String,
    val format: String,
    val height: Int,
    val id: String,
    val originCover: String,
    val playAddr: String,
    val ratio: String,
    val reflowCover: String,
    val shareCover: List<String>,
    val videoQuality: String,
    val width: Int
)