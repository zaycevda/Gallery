package com.example.gallery.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery.app.viewmodel.ScreenState.ErrorScreenState
import com.example.gallery.app.viewmodel.ScreenState.LoadingScreenState
import com.example.gallery.app.viewmodel.ScreenState.SuccessScreenState
import com.example.gallery.domain.model.Favorite
import com.example.gallery.domain.usecase.AddFavoriteUseCase
import com.example.gallery.domain.usecase.DeleteFavoriteUseCase
import com.example.gallery.domain.usecase.GetFavoriteUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val getFavoriteUseCase: GetFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : ViewModel() {
    private val _isAdded: MutableStateFlow<ScreenState<Unit>> = MutableStateFlow(value = LoadingScreenState())
    val isAdded = _isAdded.asStateFlow()

    private val _isDeleted: MutableStateFlow<ScreenState<Unit>> = MutableStateFlow(value = LoadingScreenState())
    val isDeleted = _isDeleted.asStateFlow()

    private val _favorite: MutableStateFlow<ScreenState<Favorite>> = MutableStateFlow(value = LoadingScreenState())
    val favorite = _favorite.asStateFlow()

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
            _favorite.value = ErrorScreenState(throwable = throwable)
        }

        viewModelScope.launch(context = coroutineExceptionHandler) {
            val favorite = getFavoriteUseCase.execute(favoriteId = favoriteId)
            favorite?.let { _favorite.value = SuccessScreenState(data = it) }
        }
    }
}