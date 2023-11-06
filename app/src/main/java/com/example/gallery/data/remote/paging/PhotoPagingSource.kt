package com.example.gallery.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gallery.data.remote.service.PhotoApi
import com.example.gallery.domain.model.Photo
import retrofit2.HttpException

class PhotoPagingSource(private val photoApi: PhotoApi) : PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>) =
        try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            val response = photoApi.getPhotos(page = page, pageSize = pageSize)
            if (response.isSuccessful) {
                val photos = checkNotNull(value = response.body()).map { it.toPhoto() }
                val nextKey = if (photos.size < pageSize) null else page + 1
                val prevKey = if (page == 1) null else page - 1
                LoadResult.Page(data = photos, prevKey = prevKey, nextKey = nextKey)
            } else LoadResult.Error(throwable = HttpException(response))
        } catch (e: HttpException) {
            LoadResult.Error(throwable = e)
        } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition = anchorPosition) ?: return null
        return page.prevKey?.plus(other = 1) ?: page.nextKey?.minus(other = 1)
    }
}