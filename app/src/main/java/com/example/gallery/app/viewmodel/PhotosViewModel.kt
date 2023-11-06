package com.example.gallery.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.gallery.data.remote.paging.PhotoPagingSource

class PhotosViewModel(private val pagingSource: PhotoPagingSource) : ViewModel() {
    val photos = Pager(PagingConfig(pageSize = 50)) {
        pagingSource
    }.flow.cachedIn(scope = viewModelScope)
}