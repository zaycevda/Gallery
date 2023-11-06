package com.example.gallery.data.remote.model

import com.example.gallery.domain.model.Photo

data class PhotoModel(
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
) {
    fun toPhoto() =
        Photo(
            photoId = id,
            title = title,
            imageUrl = url,
            thumbnailUrl = thumbnailUrl
        )
}