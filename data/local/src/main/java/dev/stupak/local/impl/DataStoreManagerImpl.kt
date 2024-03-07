package dev.stupak.local.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.stupak.local.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "asteroidsDataStore")


class DataStoreManagerImpl
@Inject
constructor(private val context: Context) : DataStoreManager {

    companion object {
        private val FAVOURITES_BUTTON_STATE = booleanPreferencesKey("buttonState")
    }

    override suspend fun setAddToFavouritesButtonState(state: Boolean) {
            context.dataStore.edit {preferences ->
                preferences[FAVOURITES_BUTTON_STATE] = state

            }
    }

    override fun getAddToFavouritesButtonState(): Flow<Boolean?> {
            return context.dataStore.data.map {preferences ->
                preferences[FAVOURITES_BUTTON_STATE] ?: false
            }
    }


}
