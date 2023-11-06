package com.example.gallery.data.remote.repotisory

import com.example.gallery.data.remote.service.PhotoApi
import com.example.gallery.domain.model.Photo
import com.example.gallery.domain.repository.PhotoRepository

class PhotoRepositoryImpl(private val photoApi: PhotoApi) : PhotoRepository {
    override suspend fun getPhoto(photoId: Int): Photo {
        val photoModel = photoApi.getPhoto(id = photoId)[0]
        return photoModel.toPhoto()
    }
}