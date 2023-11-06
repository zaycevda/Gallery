package com.example.gallery.app.ui.adapter

import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.gallery.R
import com.example.gallery.databinding.ItemPhotoBinding
import com.example.gallery.domain.model.Photo

private typealias OnPhotoClick = (id: Int?) -> Unit

class PhotosAdapter(
    private val onPhotoClick: OnPhotoClick
) : PagingDataAdapter<Photo, PhotosAdapter.ViewHolder>(diffCallback = DiffCallback) {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding by viewBinding(vbFactory = ItemPhotoBinding::bind)

        fun bind(photo: Photo?) {
            binding.ivPhoto.load(data = photo?.thumbnailUrl) {
                placeholder(drawable = ColorDrawable(TRANSPARENT))
                crossfade(enable = true)
            }

            itemView.setOnClickListener { onPhotoClick(photo?.photoId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = getItem(position = position)
        holder.bind(photo = photo)
    }

    private object DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem.photoId == newItem.photoId
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
    }
}