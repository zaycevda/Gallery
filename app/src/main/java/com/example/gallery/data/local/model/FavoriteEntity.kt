package com.example.gallery.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gallery.domain.model.Favorite

@Entity(tableName = "favorite_entity")
data class FavoriteEntity(
    @PrimaryKey
    val favoriteId: Int,
    val title: String,
    val imageUrl: String,
    val thumbnailUrl: String
) {
    fun toFavorite() = Favorite(
        favoriteId = favoriteId,
        title = title,
        imageUrl = imageUrl,
        thumbnailUrl = thumbnailUrl
    )
}