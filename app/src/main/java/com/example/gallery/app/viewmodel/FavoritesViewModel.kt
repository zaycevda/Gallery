package com.example.gallery.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery.app.viewmodel.ScreenState.ErrorScreenState
import com.example.gallery.app.viewmodel.ScreenState.LoadingScreenState
import com.example.gallery.app.viewmodel.ScreenState.SuccessScreenState
import com.example.gallery.domain.model.Favorite
import com.example.gallery.domain.usecase.GetFavoritesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(private val getFavoritesUseCase: GetFavoritesUseCase) : ViewModel() {
    private val _favorites: MutableStateFlow<ScreenState<List<Favorite>>> = MutableStateFlow(LoadingScreenState())
    val favorites = _favorites.asStateFlow()

    fun getFavorites() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _favorites.value = ErrorScreenState(throwable = throwable)
        }

        viewModelScope.launch(context = coroutineExceptionHandler) {
            val favorites = getFavoritesUseCase.execute()
            _favorites.value = SuccessScreenState(data = favorites)
        }
    }
}