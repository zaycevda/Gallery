package com.example.gallery.domain.usecase

import com.example.gallery.domain.repository.FavoriteRepository

class DeleteFavoriteUseCase(private val favoriteRepository: FavoriteRepository) {
    suspend fun execute(favoriteId: Int) = favoriteRepository.deleteFavorite(favoriteId = favoriteId)
}