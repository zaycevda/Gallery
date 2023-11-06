package com.example.gallery.domain.usecase

import com.example.gallery.domain.repository.FavoriteRepository

class GetFavoriteUseCase(private val favoriteRepository: FavoriteRepository) {
    suspend fun execute(favoriteId: Int) = favoriteRepository.getFavorite(favoriteId = favoriteId)
}