package com.example.gallery.domain.model

import com.example.gallery.data.local.model.FavoriteEntity

data class Favorite(
    val favoriteId: Int,
    val title: String,
    val imageUrl: String,
    val thumbnailUrl: String
) {
    fun toFavoriteEntity() = FavoriteEntity(
        favoriteId = favoriteId,
        title = title,
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl
    )
}