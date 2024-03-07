package dev.stupak.navigation.navigator

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import dev.stupak.navigation.NavGraphDirections

class Navigator {
    lateinit var navController: NavController

    fun navigateToFlow(
        navigationFlow: NavigationFlow?,
        clearBackStackEntry: Boolean = false,
        deeplink: String? = null,
    ) {
        with(navController) {
            if (clearBackStackEntry) {
                popBackStack()
            }
            if (deeplink.isNullOrEmpty()) {
                when (navigationFlow) {
                    is NavigationFlow.HostFlow-> navigateToHost()
                    is NavigationFlow.DetailsFlow-> navigateToDetails()
                    else -> {

                    }
                }
            } else {
                navigate(Uri.parse(deeplink))
            }
        }
    }


    internal fun navigateToHost() {
        navController.navigate(NavGraphDirections.actionGlobalHostFlow())
    }

    internal fun navigateToDetails() {
        navController.navigate(NavGraphDirections.actionGlobalDetailsFlow())
    }



}