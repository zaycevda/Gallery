package com.example.gallery.domain.usecase

import com.example.gallery.domain.repository.FavoriteRepository

class GetFavoritesUseCase(private val favoriteRepository: FavoriteRepository) {
    suspend fun execute() = favoriteRepository.getFavorites()
}