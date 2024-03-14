package dev.stupak.host

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.stupak.host.databinding.FragmentHostBinding
import dev.stupak.platform.base.BaseFragment


@AndroidEntryPoint
class FragmentHost : BaseFragment(R.layout.fragment_host) {
    private val binding by viewBinding(FragmentHostBinding::bind)
    private lateinit var conf: AppBarConfiguration
    private lateinit var navController: NavController

    override fun configureUi(savedInstanceState: Bundle?) {
        configureBottomNavigationBar()
    }

    private fun configureBottomNavigationBar() {
        navController =
            (childFragmentManager.findFragmentById(R.id.fragment_container_view_tabs) as NavHostFragment).navController
        conf =
            AppBarConfiguration(
                setOf(
                    R.id.item_asteroids,
                    R.id.item_favourites,
                    R.id.item_settings
                )
            )
        with(binding) {
            bottomNav.itemIconTintList = null
            bottomNav.setupWithNavController(navController)
        }
        navController.clearBackStack(R.id.fragment_host)
    }
}
