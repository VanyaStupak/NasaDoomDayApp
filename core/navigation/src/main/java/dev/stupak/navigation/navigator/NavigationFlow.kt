package dev.stupak.navigation.navigator

sealed class NavigationFlow {
    data object HostFlow : NavigationFlow()
    data object ComparisonFlow : NavigationFlow()
    data object DetailsFlow : NavigationFlow()
    data object FavouritesFlow : NavigationFlow()
}