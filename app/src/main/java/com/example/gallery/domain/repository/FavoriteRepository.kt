package com.example.gallery.domain.repository

import com.example.gallery.domain.model.Favorite

interface FavoriteRepository {
    suspend fun addFavorite(favorite: Favorite)
    suspend fun deleteFavorite(favoriteId: Int)
    suspend fun getFavorite(favoriteId: Int): Favorite?
    suspend fun getFavorites(): List<Favorite>
}