package com.example.gallery.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gallery.data.local.model.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert
    suspend fun addFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorite_entity WHERE favoriteId = :favoriteId")
    suspend fun deleteFavorite(favoriteId: Int)

    @Query("SELECT * FROM favorite_entity WHERE favoriteId = :favoriteId")
    suspend fun getFavorite(favoriteId: Int): FavoriteEntity?

    @Query("SELECT * FROM favorite_entity")
    suspend fun getFavorites(): List<FavoriteEntity>
}