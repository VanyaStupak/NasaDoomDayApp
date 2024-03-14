package dev.stupak.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import dev.stupak.domain.model.SettingsDataDomainModel
import dev.stupak.domain.model.toSettingsDataModel
import dev.stupak.local.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetSettingsDataUseCase
    @Inject
    constructor(
        private val dataStoreManager: DataStoreManager,
    ) {
        @RequiresApi(Build.VERSION_CODES.O)
        suspend operator fun invoke(data: SettingsDataDomainModel) {
            return withContext(Dispatchers.IO) {
                dataStoreManager.setSettingsData(data.toSettingsDataModel())
            }
        }
    }
