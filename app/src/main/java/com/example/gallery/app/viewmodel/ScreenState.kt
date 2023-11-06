package com.example.gallery.app.viewmodel

private typealias OnError = (Throwable) -> Unit
private typealias OnLoading = () -> Unit
private typealias OnSuccess<T> = (T) -> Unit

sealed class ScreenState<T> {
    private val onErrorEmpty: OnError = {}
    private val onLoadingEmpty: OnLoading = {}
    private val onSuccessEmpty: OnSuccess<T> = {}

    fun on(
        error: OnError = onErrorEmpty,
        loading: OnLoading = onLoadingEmpty,
        success: OnSuccess<T> = onSuccessEmpty
    ) {
        when (this) {
            is ErrorScreenState -> onError(doOnError =  error)
            is LoadingScreenState -> loading()
            is SuccessScreenState -> success(data)
        }
    }

    class ErrorScreenState<T>(val throwable: Throwable) : ScreenState<T>() {
        private var isShowedError = false

        fun onError(doOnError: (Throwable) -> Unit) {
            if (!isShowedError) {
                doOnError(throwable)
                isShowedError = true
            }
        }
    }

    class LoadingScreenState<T> : ScreenState<T>()
    class SuccessScreenState<T>(val data: T) : ScreenState<T>()
}