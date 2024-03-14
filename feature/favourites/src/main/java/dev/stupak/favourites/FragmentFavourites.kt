package dev.stupak.favourites

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.stupak.favourites.adapter.FavouritesAdapter
import dev.stupak.favourites.databinding.FragmentFavouritesBinding
import dev.stupak.platform.base.BaseFragment
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentFavourites : BaseFragment(R.layout.fragment_favourites) {
    private val binding by viewBinding(FragmentFavouritesBinding::bind)
    private lateinit var adapter: FavouritesAdapter
    private val viewModel: FavouritesViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun configureUi(savedInstanceState: Bundle?) {
        activity?.window?.statusBarColor = Color.WHITE
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.asteroidsListStateFlow.collect { asteroids ->
                if (asteroids.isNotEmpty()) {
                    binding.tvEmpty.visibility = View.GONE
                    adapter.setData(asteroids)
                } else {
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }
        }
    }
}
