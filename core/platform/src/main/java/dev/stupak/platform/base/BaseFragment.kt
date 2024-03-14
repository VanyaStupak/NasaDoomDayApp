package dev.stupak.platform.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import dev.stupak.navigation.navigator.NavigationFlow
import dev.stupak.navigation.navigator.ToFlowNavigable

abstract class BaseFragment(
    @LayoutRes layout: Int,
) : Fragment(layout), ToFlowNavigable {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        configureUi(savedInstanceState)
    }

    override fun navigateToFlow(
        flow: NavigationFlow?,
        clearBackStackEntry: Boolean,
        deeplink: String?,
        argument: String?,
    ) {
        try {
            (requireActivity() as? ToFlowNavigable)?.navigateToFlow(
                flow,
                clearBackStackEntry,
                deeplink,
                argument,
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    protected open fun configureUi(savedInstanceState: Bundle?) = Unit
}
