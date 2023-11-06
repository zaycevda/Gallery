package com.example.gallery.app.ui.screen

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.gallery.R
import com.example.gallery.app.App
import com.example.gallery.app.ui.util.showSnackbar
import com.example.gallery.app.viewmodel.FavoriteViewModel
import com.example.gallery.app.viewmodel.viewModelFactory
import com.example.gallery.databinding.FragmentFavoriteBinding
import com.example.gallery.domain.model.Favorite
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private val binding by viewBinding(vbFactory = FragmentFavoriteBinding::bind)
    private var favorite: Favorite? = null
    private val favoriteId by lazy { arguments?.getInt(FAVORITE_ID_KEY) }
    private val viewModel by viewModels<FavoriteViewModel> {
        viewModelFactory(
            initializer = FavoriteViewModel(
                getFavoriteUseCase = App.roomModule.getFavoriteUseCase,
                addFavoriteUseCase = App.roomModule.addFavoriteUseCase,
                deleteFavoriteUseCase = App.roomModule.deleteFavoriteUseCase
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFavorite()
        workWithFavorite()
    }

    private fun getFavorite() {
        favoriteId?.let { favoriteId ->
            viewModel.getFavorite(favoriteId = favoriteId)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.favorite.collect { state ->
                    state.on(
                        error = { throwable ->
                            binding.root.showSnackbar(message = throwable.localizedMessage)
                            binding.pbFavorite.isGone = true
                            binding.container.isGone = false
                        },
                        loading = {
                            binding.pbFavorite.isGone = false
                            binding.container.isGone = true
                        },
                        success = { favorite ->
                            this@FavoriteFragment.favorite = favorite

                            binding.ivFavorite.load(data = favorite.imageUrl)
                            binding.tvTitle.text = favorite.title
                            binding.ivFavoriteIcon.isSelected = true
                            binding.pbFavorite.isGone = true
                            binding.container.isGone = false
                        }
                    )
                }
            }
        }
    }

    private fun workWithFavorite() {
        binding.ivFavoriteIcon.setOnClickListener {
            if (binding.ivFavoriteIcon.isSelected) deleteFavorite()
            else addFavorite()
        }
    }

    private fun addFavorite() {
        favorite?.let { favorite ->
            viewModel.addFavorite(favorite = favorite)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.isAdded.collect { state ->
                    state.on(
                        error = { throwable ->
                            binding.root.showSnackbar(message = throwable.localizedMessage)
                        },
                        success = {
                            binding.ivFavoriteIcon.setImageDrawable(
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.ic_favorite_enable,
                                    null
                                )
                            )
                            binding.ivFavoriteIcon.isSelected = true
                        }
                    )
                }
            }
        }
    }

    private fun deleteFavorite() {
        favoriteId?.let { favoriteId ->
            viewModel.deleteFavorite(favoriteId = favoriteId)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.isDeleted.collect { state ->
                    state.on(
                        error = { throwable ->
                            binding.root.showSnackbar(message = throwable.localizedMessage)
                        },
                        success = {
                            binding.ivFavoriteIcon.setImageDrawable(
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.ic_favorite_disable,
                                    null
                                )
                            )
                            binding.ivFavoriteIcon.isSelected = false
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val FAVORITE_ID_KEY = "FAVORITE_ID_KEY"
    }
}