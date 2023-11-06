package com.example.gallery.app.ui.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(message: String?) {
    Snackbar.make(this, message ?: "", Snackbar.LENGTH_SHORT).show()
}