package dev.stupak.local.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import dev.stupak.local.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class SettingsDataModel(
    val diameterUnit: String?,
    val velocityUnit: String?,
    val distanceUnit: String?,
    val pushInterval: String?,
)

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "asteroidsDataStore")

class DataStoreManagerImpl
    @Inject
    constructor(private val context: Context) : DataStoreManager {
        private val gson: Gson = GsonBuilder().setLenient().create()

        companion object {
            private val SETTINGS_DATA = stringPreferencesKey("settingsData")
        }

        override suspend fun setSettingsData(data: SettingsDataModel) {
            setValue(SETTINGS_DATA, data)
        }

        override fun getSettingsData(): Flow<SettingsDataModel?> {
            return getValue<SettingsDataModel>(SETTINGS_DATA)
        }

        private suspend inline fun <reified T> setValue(
            key: Preferences.Key<String>,
            value: T,
        ) {
            val adapter = gson.getAdapter(T::class.java)
            val json = adapter.toJson(value)

            context.dataStore.edit { preferences ->
                preferences[key] = json
            }
        }

        private inline fun <reified T> getValue(key: Preferences.Key<String>): Flow<T?> {
            val adapter = gson.getAdapter(T::class.java)

            return context.dataStore.data.map { preferences ->
                val value = preferences[key]

                if (value == null) {
                    return@map SettingsDataModel(null, null, null, null) as T?
                } else {
                    try {
                        adapter.fromJson(value)
                    } catch (ex: JsonParseException) {
                        null
                    }
                }
            }
        }
    }
