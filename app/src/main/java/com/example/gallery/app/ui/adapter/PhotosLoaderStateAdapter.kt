package com.example.gallery.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gallery.R
import com.example.gallery.databinding.ItemErrorBinding

class PhotosLoaderStateAdapter : LoadStateAdapter<PhotosLoaderStateAdapter.ViewHolder>() {
    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private class ProgressViewHolder(view: View) : ViewHolder(view = view)

    private class ErrorViewHolder(view: View) : ViewHolder(view = view) {
        private val binding by viewBinding(vbFactory = ItemErrorBinding::bind)

        fun bind(loadState: LoadState) {
            require(value = loadState is LoadState.Error)
            binding.tvError.text = loadState.error.localizedMessage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        when (loadState) {
            is LoadState.NotLoading -> error(message = "Not supported")
            is LoadState.Loading -> {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_progress, parent, false)
                ProgressViewHolder(view = view)
            }

            is LoadState.Error -> {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_error, parent, false)
                ErrorViewHolder(view = view)
            }
        }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        when (holder.itemViewType) {
            PROGRESS -> Unit
            ERROR -> (holder as ErrorViewHolder).bind(loadState = loadState)
        }
    }

    override fun getStateViewType(loadState: LoadState) =
        when (loadState) {
            is LoadState.NotLoading -> error(message = "Not supported")
            is LoadState.Loading -> PROGRESS
            is LoadState.Error -> ERROR
        }

    private companion object {
        private const val PROGRESS = 0
        private const val ERROR = 1
    }
}