package com.example.gallery.app.ui.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.R

class RecyclerViewItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as? GridLayoutManager
        val spanCount = layoutManager?.spanCount ?: 1
        val itemWidth = context.resources.getDimensionPixelSize(R.dimen.item_width)

        val horizontalMargin = (parent.width - (itemWidth * spanCount)) / (spanCount + 1)
        outRect.bottom = horizontalMargin
    }
}