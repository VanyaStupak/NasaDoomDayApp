package dev.stupak.local

import dev.stupak.local.impl.SettingsDataModel
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun setSettingsData(data: SettingsDataModel)

    fun getSettingsData(): Flow<SettingsDataModel?>
}
