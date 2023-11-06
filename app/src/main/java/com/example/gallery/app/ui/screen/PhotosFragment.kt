package com.example.gallery.app.ui.screen

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gallery.R
import com.example.gallery.app.App
import com.example.gallery.app.ui.adapter.PhotosAdapter
import com.example.gallery.app.ui.adapter.PhotosLoaderStateAdapter
import com.example.gallery.app.ui.util.RecyclerViewItemDecoration
import com.example.gallery.app.ui.util.showSnackbar
import com.example.gallery.app.viewmodel.PhotosViewModel
import com.example.gallery.app.viewmodel.viewModelFactory
import com.example.gallery.databinding.FragmentPhotosBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PhotosFragment : Fragment(R.layout.fragment_photos) {
    private var adapter: PhotosAdapter? = null
    private val binding by viewBinding(vbFactory = FragmentPhotosBinding::bind)
    private val viewModel by viewModels<PhotosViewModel> {
        viewModelFactory(
            initializer = PhotosViewModel(
                pagingSource = App.retrofitModule.photoPagingSource
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        getPhotos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    private fun initAdapter() {
        adapter = PhotosAdapter { photoId ->
            findNavController().navigate(
                resId = R.id.action_photosFragment_to_photoFragment,
                args = bundleOf(PhotoFragment.PHOTO_ID_KEY to photoId)
            )
        }

        adapter?.addLoadStateListener { state ->
            val refreshState = state.refresh
            binding.rvPhotos.isGone = refreshState == LoadState.Loading
            binding.pbPhotos.isGone = refreshState != LoadState.Loading

            if (refreshState is LoadState.Error)
                binding.root.showSnackbar(message = refreshState.error.localizedMessage)
        }

        val displayWidth = resources.displayMetrics.widthPixels
        val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
        val spanCount = displayWidth / itemWidth

        binding.rvPhotos.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.rvPhotos.setHasFixedSize(true)
        binding.rvPhotos.addItemDecoration(RecyclerViewItemDecoration(requireContext()))
        binding.rvPhotos.adapter = adapter?.withLoadStateHeader(header = PhotosLoaderStateAdapter())
    }

    private fun getPhotos() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.photos.collectLatest { pagingPhoto ->
                    adapter?.submitData(pagingData = pagingPhoto)
                }
            }
        }
    }
}