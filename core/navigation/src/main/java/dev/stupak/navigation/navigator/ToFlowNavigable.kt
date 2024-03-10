package dev.stupak.navigation.navigator

interface ToFlowNavigable {
    fun navigateToFlow(
        flow: NavigationFlow? = null,
        clearBackStackEntry: Boolean = false,
        deeplink: String? = null,
        argument: String? = null
    )

}
