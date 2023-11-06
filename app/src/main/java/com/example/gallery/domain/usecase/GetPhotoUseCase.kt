package com.example.gallery.domain.usecase

import com.example.gallery.domain.repository.PhotoRepository

class GetPhotoUseCase(private val photoRepository: PhotoRepository) {
    suspend fun execute(photoId: Int) = photoRepository.getPhoto(photoId = photoId)
}