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
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gallery.R
import com.example.gallery.app.App
import com.example.gallery.app.ui.adapter.FavoritesAdapter
import com.example.gallery.app.ui.util.RecyclerViewItemDecoration
import com.example.gallery.app.ui.util.showSnackbar
import com.example.gallery.app.viewmodel.FavoritesViewModel
import com.example.gallery.app.viewmodel.viewModelFactory
import com.example.gallery.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private var adapter: FavoritesAdapter? = null
    private val binding by viewBinding(vbFactory = FragmentFavoritesBinding::bind)
    private val viewModel by viewModels<FavoritesViewModel> {
        viewModelFactory(
            initializer = FavoritesViewModel(
                getFavoritesUseCase = App.roomModule.getFavoritesUseCase
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        getFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    private fun initAdapter() {
        adapter = FavoritesAdapter { favoriteId ->
            findNavController().navigate(
                resId = R.id.action_favoritesFragment_to_favoriteFragment,
                args = bundleOf(FavoriteFragment.FAVORITE_ID_KEY to favoriteId)
            )
        }

        val displayWidth = resources.displayMetrics.widthPixels
        val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
        val spanCount = displayWidth / itemWidth

        binding.rvFavorites.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.rvFavorites.setHasFixedSize(true)
        binding.rvFavorites.addItemDecoration(RecyclerViewItemDecoration(requireContext()))
        binding.rvFavorites.adapter = adapter
    }

    private fun getFavorites() {
        viewModel.getFavorites()
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.favorites.collect { state ->
                    state.on(
                        error = { throwable ->
                            binding.root.showSnackbar(message = throwable.localizedMessage)
                            binding.pbFavorites.isGone = true
                            binding.rvFavorites.isGone = false
                        },
                        loading = {
                            binding.pbFavorites.isGone = false
                            binding.rvFavorites.isGone = true
                        },
                        success = { favorites ->
                            adapter?.favorites = favorites

                            binding.pbFavorites.isGone = true
                            binding.rvFavorites.isGone = false
                        }
                    )
                }
            }
        }
    }
}