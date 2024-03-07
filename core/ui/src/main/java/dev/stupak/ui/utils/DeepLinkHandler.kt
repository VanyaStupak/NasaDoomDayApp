package dev.stupak.ui.utils

import android.annotation.SuppressLint
import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest

object DeepLinkHandler {
    @SuppressLint("RestrictedApi")
    fun handleDeepLink(
        navController: NavController,
        deepLink: Uri,
    ) {
        val request = NavDeepLinkRequest.Builder.fromUri(deepLink).build()
        val graph = navController.graph
        val deepLinkMatch = graph.matchDeepLink(request)

        if (deepLinkMatch != null) {
            navController.navigate(deepLink)
        }
    }
}