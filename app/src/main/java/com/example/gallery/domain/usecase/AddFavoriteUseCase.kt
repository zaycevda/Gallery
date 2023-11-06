package com.example.gallery.domain.usecase

import com.example.gallery.domain.model.Favorite
import com.example.gallery.domain.repository.FavoriteRepository

class AddFavoriteUseCase(private val favoriteRepository: FavoriteRepository) {
    suspend fun execute(favorite: Favorite) = favoriteRepository.addFavorite(favorite = favorite)
}