package dev.stupak.host

import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.stupak.host.databinding.ActivityHostBinding
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.platform.base.BaseActivity
import dev.stupak.worker.AsteroidWorker

@AndroidEntryPoint
class HostActivity : BaseActivity(R.layout.activity_host) {
    private val binding by viewBinding(ActivityHostBinding::bind)

    override fun configureUi() {
        initializeNavController()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "MyWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            AsteroidWorker.createPeriodicRequest(),
        )

        if (isFirstRun()) {
            navigateToFlow(NavigationFlow.OnboardingFlow)
        }

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
        argument: String?
    ) {
        navigator.navigateToFlow(flow, clearBackStackEntry, deeplink, argument)
    }
}