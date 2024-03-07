package dev.stupak.navigation.navigator

sealed class NavigationFlow {
    data object HostFlow : NavigationFlow()
    data object AsteroidsFlow : NavigationFlow()
    data object FavouritesFlow : NavigationFlow()
    data object DetailsFlow : NavigationFlow()
}