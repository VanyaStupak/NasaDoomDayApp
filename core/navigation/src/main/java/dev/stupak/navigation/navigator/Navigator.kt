package dev.stupak.navigation.navigator

import android.net.Uri
import androidx.navigation.NavController
import dev.stupak.navigation.NavGraphDirections

class Navigator {
    lateinit var navController: NavController

    fun navigateToFlow(
        navigationFlow: NavigationFlow?,
        clearBackStackEntry: Boolean = false,
        deeplink: String? = null,
        argument: String? = null
    ) {
        with(navController) {
            if (clearBackStackEntry) {
                popBackStack()
            }
            if (deeplink.isNullOrEmpty()) {
                when (navigationFlow) {
                    is NavigationFlow.ComparisonFlow -> navigateToComparison(argument ?: "")
                    is NavigationFlow.HostFlow -> navigateToHost()
                    is NavigationFlow.DetailsFlow -> navigateToDetails()
                    is NavigationFlow.FavouritesFlow -> navigateToFavourites()
                    is NavigationFlow.OnboardingFlow -> navigateToOnboarding()
                    else -> {}
                }
            } else {
                navigate(Uri.parse(deeplink))
            }
        }
    }


    internal fun navigateToHost() {
        navController.currentDestination?.id?.let { navController.popBackStack(it, true) }
        navController.navigate(NavGraphDirections.actionGlobalHostFlow())
    }

    internal fun navigateToFavourites() {
        navController.navigate(NavGraphDirections.actionGlobalFavouritesFlow())
    }

    internal fun navigateToDetails() {
        navController.navigate(NavGraphDirections.actionGlobalDetailsFlow())
    }

    internal fun navigateToOnboarding() {
        navController.navigate(NavGraphDirections.actionGlobalOnboardingFlow())
    }

    internal fun navigateToComparison(asteroidId: String) {
        navController.navigate(NavGraphDirections.actionGlobalComparisonFlow().apply {
            arguments.putString("id", asteroidId)
        })
    }


}