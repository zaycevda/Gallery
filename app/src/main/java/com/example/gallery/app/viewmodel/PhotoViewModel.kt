package com.example.gallery.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery.app.viewmodel.ScreenState.ErrorScreenState
import com.example.gallery.app.viewmodel.ScreenState.LoadingScreenState
import com.example.gallery.app.viewmodel.ScreenState.SuccessScreenState
import com.example.gallery.domain.model.Favorite
import com.example.gallery.domain.model.Photo
import com.example.gallery.domain.usecase.AddFavoriteUseCase
import com.example.gallery.domain.usecase.DeleteFavoriteUseCase
import com.example.gallery.domain.usecase.GetFavoriteUseCase
import com.example.gallery.domain.usecase.GetPhotoUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoViewModel(
    private val getPhotoUseCase: GetPhotoUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val getFavoriteUseCase: GetFavoriteUseCase
) : ViewModel() {
    private val _photo: MutableStateFlow<ScreenState<Photo>> = MutableStateFlow(value = LoadingScreenState())
    val photo = _photo.asStateFlow()

    private val _isAdded: MutableStateFlow<ScreenState<Unit>> = MutableStateFlow(value = LoadingScreenState())
    val isAdded = _isAdded.asStateFlow()

    private val _isDeleted: MutableStateFlow<ScreenState<Unit>> = MutableStateFlow(value = LoadingScreenState())
    val isDeleted = _isDeleted.asStateFlow()

    private val _isFavoriteExist: MutableStateFlow<ScreenState<Boolean>> = MutableStateFlow(value = LoadingScreenState())
    val isFavoriteExist = _isFavoriteExist.asStateFlow()

    fun getPhoto(photoId: Int) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _photo.value = ErrorScreenState(throwable = throwable)
        }

        viewModelScope.launch(context = coroutineExceptionHandler) {
            val photo = getPhotoUseCase.execute(photoId = photoId)
            _photo.value = SuccessScreenState(data = photo)
        }
    }

    fun addFavorite(favorite: Favorite) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _isAdded.value = ErrorScreenState(throwable = throwable)
        }

        viewModelScope.launch(context = coroutineExceptionHandler) {
            val isAdded = addFavoriteUseCase.execute(favorite = favorite)
            _isAdded.value = SuccessScreenState(data = isAdded)
        }
    }

    fun deleteFavorite(favoriteId: Int) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _isDeleted.value = ErrorScreenState(throwable = throwable)
        }

        viewModelScope.launch(context = coroutineExceptionHandler) {
            val isDeleted = deleteFavoriteUseCase.execute(favoriteId = favoriteId)
            _isDeleted.value = SuccessScreenState(data = isDeleted)
        }
    }

    fun getFavorite(favoriteId: Int) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _isFavoriteExist.value = ErrorScreenState(throwable = throwable)
        }

        viewModelScope.launch(context = coroutineExceptionHandler) {
            val favorite = getFavoriteUseCase.execute(favoriteId = favoriteId)
            _isFavoriteExist.value = SuccessScreenState(data = favorite != null)
        }
    }
}