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
import com.example.gallery.app.viewmodel.PhotoViewModel
import com.example.gallery.app.viewmodel.viewModelFactory
import com.example.gallery.databinding.FragmentPhotoBinding
import com.example.gallery.domain.model.Favorite
import com.example.gallery.domain.model.Photo
import kotlinx.coroutines.launch

class PhotoFragment : Fragment(R.layout.fragment_photo) {
    private val binding by viewBinding(vbFactory = FragmentPhotoBinding::bind)
    private var favorite: Favorite? = null
    private val photoId by lazy { arguments?.getInt(PHOTO_ID_KEY) }
    private val viewModel by viewModels<PhotoViewModel> {
        viewModelFactory(
            initializer = PhotoViewModel(
                getPhotoUseCase = App.retrofitModule.getPhotoUseCase,
                addFavoriteUseCase = App.roomModule.addFavoriteUseCase,
                deleteFavoriteUseCase = App.roomModule.deleteFavoriteUseCase,
                getFavoriteUseCase = App.roomModule.getFavoriteUseCase
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPhoto()
        getFavorite()
        workWithFavorite()
    }

    private fun getPhoto() {
        photoId?.let { photoId ->
            viewModel.getPhoto(photoId = photoId)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.photo.collect { state ->
                    state.on(
                        error = { throwable ->
                            binding.root.showSnackbar(message = throwable.localizedMessage)
                            binding.container.isGone = false
                            binding.pbPhoto.isGone = true
                        },
                        loading = {
                            binding.container.isGone = true
                            binding.pbPhoto.isGone = false
                        },
                        success = { photo ->
                            favorite = Favorite(
                                favoriteId = photo.photoId,
                                title = photo.title,
                                imageUrl = photo.imageUrl,
                                thumbnailUrl = photo.thumbnailUrl
                            )
                            initPhoto(photo)

                            binding.container.isGone = false
                            binding.pbPhoto.isGone = true
                        }
                    )
                }
            }
        }
    }

    private fun initPhoto(photo: Photo) {
        binding.ivPhoto.load(data = photo.imageUrl)
        binding.tvTitle.text = photo.title
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
        photoId?.let { photoId ->
            viewModel.deleteFavorite(favoriteId = photoId)
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

    private fun getFavorite() {
        photoId?.let { photoId ->
            viewModel.getFavorite(favoriteId = photoId)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.isFavoriteExist.collect { state ->
                    state.on(
                        error = { throwable ->
                            binding.root.showSnackbar(message = throwable.localizedMessage)
                        },
                        success = { isFavoriteExist ->
                            if (isFavoriteExist) {
                                binding.ivFavoriteIcon.setImageDrawable(
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.ic_favorite_enable,
                                        null
                                    )
                                )
                                binding.ivFavoriteIcon.isSelected = true
                            } else {
                                binding.ivFavoriteIcon.setImageDrawable(
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.ic_favorite_disable,
                                        null
                                    )
                                )
                                binding.ivFavoriteIcon.isSelected = false
                            }
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val PHOTO_ID_KEY = "PHOTO_ID_KEY"
    }
}