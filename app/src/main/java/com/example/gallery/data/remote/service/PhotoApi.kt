package com.example.gallery.data.remote.service

import androidx.annotation.IntRange
import com.example.gallery.data.remote.model.PhotoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {
    @GET("/photos")
    suspend fun getPhotos(
        @Query(value = "page") @IntRange(from = 1) page: Int = 1,
        @Query(value = "pageSize") @IntRange(from = 1, to = MAX_PAGE_SIZE.toLong()) pageSize: Int = DEFAULT_PAGE_SIZE,
    ): Response<List<PhotoModel>>

    @GET("/photos")
    suspend fun getPhoto(@Query("id") @IntRange(from = 1, to = 5000) id: Int): List<PhotoModel>

    private companion object {
        private const val DEFAULT_PAGE_SIZE = 20
        private const val MAX_PAGE_SIZE = 20
    }
}