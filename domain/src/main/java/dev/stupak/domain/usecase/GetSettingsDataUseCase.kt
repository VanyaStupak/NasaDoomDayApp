package dev.stupak.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import dev.stupak.domain.model.SettingsDataDomainModel
import dev.stupak.domain.model.toSettingsDataDomainModel
import dev.stupak.local.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSettingsDataUseCase
    @Inject
    constructor(
        private val dataStoreManager: DataStoreManager,
    ) {
        @RequiresApi(Build.VERSION_CODES.O)
        suspend operator fun invoke(): Flow<SettingsDataDomainModel>  {
            return withContext(Dispatchers.IO) {
                dataStoreManager.getSettingsData().map { it?.toSettingsDataDomainModel()!! }
            }
        }
    }
