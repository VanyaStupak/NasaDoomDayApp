package dev.stupak.local

import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun setAddToFavouritesButtonState(state: Boolean)
    fun getAddToFavouritesButtonState():Flow<Boolean?>
}
