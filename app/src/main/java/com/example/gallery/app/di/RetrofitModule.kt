package com.example.gallery.app.di

import com.example.gallery.data.remote.paging.PhotoPagingSource
import com.example.gallery.data.remote.repotisory.PhotoRepositoryImpl
import com.example.gallery.data.remote.service.PhotoApi
import com.example.gallery.domain.repository.PhotoRepository
import com.example.gallery.domain.usecase.GetPhotoUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface RetrofitModule {
    val photoApi: PhotoApi
    val photoPagingSource: PhotoPagingSource
    val photoRepository: PhotoRepository
    val getPhotoUseCase: GetPhotoUseCase
}

object RetrofitModuleImpl : RetrofitModule {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com"

    override val photoApi: PhotoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotoApi::class.java)
    }

    override val photoPagingSource: PhotoPagingSource by lazy {
        PhotoPagingSource(photoApi = photoApi)
    }

    override val photoRepository: PhotoRepository by lazy {
        PhotoRepositoryImpl(photoApi = photoApi)
    }

    override val getPhotoUseCase: GetPhotoUseCase by lazy {
        GetPhotoUseCase(photoRepository = photoRepository)
    }
}