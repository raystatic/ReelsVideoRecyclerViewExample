package com.raystatic.videoexoplayer.data.responses.pixabay

data class PixabayVideosResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)