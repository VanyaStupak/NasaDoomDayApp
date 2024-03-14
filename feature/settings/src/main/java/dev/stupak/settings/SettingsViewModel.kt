package dev.stupak.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stupak.domain.usecase.GetSettingsDataUseCase
import dev.stupak.domain.usecase.SetSettingsDataUseCase
import dev.stupak.settings.model.SettingsDataUIModel
import dev.stupak.settings.model.toSettingsDataDomainModel
import dev.stupak.settings.model.toSettingsUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val setSettingsDataUseCase: SetSettingsDataUseCase,
        private val getSettingsDataUseCase: GetSettingsDataUseCase,
    ) : ViewModel() {
        private val _settingsStateFlow =
            MutableStateFlow(
                SettingsDataUIModel(null, null, null, null),
            )
        val settingsStateFlow = _settingsStateFlow.asStateFlow()

        init {
            getSettingsData()
        }

        private fun getSettingsData() {
            viewModelScope.launch {
                getSettingsDataUseCase.invoke().collect { settingsData ->
                    _settingsStateFlow.update { settingsData.toSettingsUIModel() }
                }
            }
        }

        fun setSettingsData(data: SettingsDataUIModel) {
            viewModelScope.launch {
                setSettingsDataUseCase.invoke(data.toSettingsDataDomainModel())
            }
        }
    }
