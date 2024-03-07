package dev.stupak.favourites

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.stupak.favourites.adapter.FavouritesAdapter
import dev.stupak.favourites.databinding.FragmentFavouritesBinding
import dev.stupak.favourites.model.FavouritesUIModel
import dev.stupak.platform.base.BaseFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentFavourites : BaseFragment(R.layout.fragment_favourites) {

    private val binding by viewBinding(FragmentFavouritesBinding::bind)
    private lateinit var adapter: FavouritesAdapter
    private val viewModel: FavouritesViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureUi(savedInstanceState: Bundle?) {
        adapter =
            FavouritesAdapter(emptyList()) { asteroidId ->
                navigateToFlow(null, false, "asteroids://app/$asteroidId/favourites")
            }

        binding.apply {
            rvFavourites.layoutManager = LinearLayoutManager(requireContext())
            rvFavourites.adapter = adapter

        }
            observeFavouritesData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeFavouritesData() {
        lifecycleScope.launch {
            viewModel.asteroidsListStateFlow.collect { asteroids ->
                if (asteroids.isNotEmpty()) {
                    adapter.setData(asteroids)
                }
            }
        }
    }
}