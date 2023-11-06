package com.example.gallery.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gallery.data.local.dao.FavoriteDao
import com.example.gallery.data.local.model.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1
)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract val favoriteDao: FavoriteDao
}