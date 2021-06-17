package com.raystatic.videoexoplayer.data.model

data class Author(
    val avatarLarger: String,
    val avatarMedium: String,
    val avatarThumb: String,
    val commentSetting: Int,
    val duetSetting: Int,
    val ftc: Boolean,
    val id: String,
    val nickname: String,
    val openFavorite: Boolean,
    val privateAccount: Boolean,
    val relation: Int,
    val secUid: String,
    val secret: Boolean,
    val signature: String,
    val stitchSetting: Int,
    val uniqueId: String,
    val verified: Boolean
)