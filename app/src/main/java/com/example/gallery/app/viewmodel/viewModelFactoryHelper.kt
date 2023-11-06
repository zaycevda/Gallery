package com.example.gallery.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <VM : ViewModel> viewModelFactory(initializer: VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>) = initializer as T
    }