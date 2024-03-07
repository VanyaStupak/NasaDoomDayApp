package dev.stupak.host



import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.stupak.host.databinding.ActivityHostBinding
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.platform.base.BaseActivity

@AndroidEntryPoint
class HostActivity : BaseActivity(R.layout.activity_host) {
    private val binding by viewBinding(ActivityHostBinding::bind)

    override fun configureUi() {
        initializeNavController()
    }
    private fun initializeNavController() {
        (
                supportFragmentManager
                    .findFragmentById(binding.fragmentContainerView.id) as? NavHostFragment
                )?.let { navHostFragment ->
                navHostFragment.navController.let { navController ->
                    navigator.navController = navController
                }
            }
    }

    override fun navigateToFlow(
        flow: NavigationFlow?,
        clearBackStackEntry: Boolean,
        deeplink: String?,
    ) {
        navigator.navigateToFlow(flow, clearBackStackEntry, deeplink)
    }
}