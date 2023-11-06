package com.example.gallery.domain.repository

import com.example.gallery.domain.model.Photo

interface PhotoRepository {
    suspend fun getPhoto(photoId: Int): Photo
}