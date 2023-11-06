package com.example.gallery.app.ui.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.gallery.R
import com.example.gallery.app.ui.adapter.FavoritesAdapter.ViewHolder
import com.example.gallery.databinding.ItemFavoriteBinding
import com.example.gallery.domain.model.Favorite

private typealias OnFavoriteClick = (favoriteId: Int) -> Unit

class FavoritesAdapter(
    private val onFavoriteClick: OnFavoriteClick
) : RecyclerView.Adapter<ViewHolder>() {
    private val differ = AsyncListDiffer(this, DiffCallback)
    var favorites: List<Favorite>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding by viewBinding(vbFactory = ItemFavoriteBinding::bind)

        fun bind(favorite: Favorite) {
            binding.ivFavorite.load(data = favorite.thumbnailUrl) {
                placeholder(drawable = ColorDrawable(Color.TRANSPARENT))
                crossfade(enable = true)
            }

            itemView.setOnClickListener { onFavoriteClick(favorite.favoriteId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favorites[position]
        holder.bind(favorite = favorite)
    }

    override fun getItemCount() = favorites.count()

    private object DiffCallback : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite) = oldItem.favoriteId == newItem.favoriteId
        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite) = oldItem == newItem
    }
}