package com.example.gallery.domain.model

data class Photo(
    val photoId: Int,
    val title: String,
    val imageUrl: String,
    val thumbnailUrl: String
)