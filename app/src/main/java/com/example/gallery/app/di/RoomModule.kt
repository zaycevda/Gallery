package com.example.gallery.app.di

import android.content.Context
import androidx.room.Room
import com.example.gallery.data.local.database.FavoriteDatabase
import com.example.gallery.data.local.repository.FavoriteRepositoryImpl
import com.example.gallery.domain.repository.FavoriteRepository
import com.example.gallery.domain.usecase.AddFavoriteUseCase
import com.example.gallery.domain.usecase.DeleteFavoriteUseCase
import com.example.gallery.domain.usecase.GetFavoriteUseCase
import com.example.gallery.domain.usecase.GetFavoritesUseCase

interface RoomModule {
    val favoriteDatabase: FavoriteDatabase
    val favoriteRepository: FavoriteRepository
    val addFavoriteUseCase: AddFavoriteUseCase
    val deleteFavoriteUseCase: DeleteFavoriteUseCase
    val getFavoriteUseCase: GetFavoriteUseCase
    val getFavoritesUseCase: GetFavoritesUseCase
}

class RoomModuleImpl(private val context: Context) : RoomModule {
    override val favoriteDatabase: FavoriteDatabase by lazy {
        Room.databaseBuilder(
            context = context,
            klass = FavoriteDatabase::class.java,
            name = "favorite.db"
        ).build()
    }

    override val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepositoryImpl(favoriteDatabase = favoriteDatabase)
    }

    override val addFavoriteUseCase: AddFavoriteUseCase by lazy {
        AddFavoriteUseCase(favoriteRepository = favoriteRepository)
    }

    override val deleteFavoriteUseCase: DeleteFavoriteUseCase by lazy {
        DeleteFavoriteUseCase(favoriteRepository = favoriteRepository)
    }

    override val getFavoriteUseCase: GetFavoriteUseCase by lazy {
        GetFavoriteUseCase(favoriteRepository = favoriteRepository)
    }

    override val getFavoritesUseCase: GetFavoritesUseCase by lazy {
        GetFavoritesUseCase(favoriteRepository = favoriteRepository)
    }
}