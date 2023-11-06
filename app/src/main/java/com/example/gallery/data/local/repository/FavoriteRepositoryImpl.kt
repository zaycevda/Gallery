package com.example.gallery.data.local.repository

import com.example.gallery.data.local.database.FavoriteDatabase
import com.example.gallery.domain.model.Favorite
import com.example.gallery.domain.repository.FavoriteRepository

class FavoriteRepositoryImpl(favoriteDatabase: FavoriteDatabase) : FavoriteRepository {
    private val favoriteDao = favoriteDatabase.favoriteDao

    override suspend fun addFavorite(favorite: Favorite) {
        val favoriteEntity = favorite.toFavoriteEntity()
        favoriteDao.addFavorite(favoriteEntity = favoriteEntity)
    }

    override suspend fun deleteFavorite(favoriteId: Int) {
        favoriteDao.deleteFavorite(favoriteId = favoriteId)
    }

    override suspend fun getFavorite(favoriteId: Int): Favorite? {
        val favoriteEntity = favoriteDao.getFavorite(favoriteId = favoriteId)
        return favoriteEntity?.toFavorite()
    }

    override suspend fun getFavorites(): List<Favorite> {
        val favoriteEntities = favoriteDao.getFavorites()
        return favoriteEntities.map { favoriteEntity -> favoriteEntity.toFavorite() }
    }
}