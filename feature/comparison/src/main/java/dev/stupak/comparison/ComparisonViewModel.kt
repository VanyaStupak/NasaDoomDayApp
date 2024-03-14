package dev.stupak.comparison

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stupak.comparison.model.PagerUIModel
import dev.stupak.comparison.model.SettingsDataUIModel
import dev.stupak.comparison.model.toPagerUiModel
import dev.stupak.comparison.model.toSettingsUIModel
import dev.stupak.domain.usecase.GetFavouritesListUseCase
import dev.stupak.domain.usecase.GetSettingsDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ComparisonViewModel
    @Inject
    constructor(
        private val getFavouritesListUseCase: GetFavouritesListUseCase,
        private val getSettingsDataUseCase: GetSettingsDataUseCase,
    ) : ViewModel() {
        data class ComparisonViewState(
            val favourites: List<PagerUIModel>,
            val settingsData: SettingsDataUIModel,
        )

        private val _comparisonStateFlow =
            MutableStateFlow(
                ComparisonViewState(
                    emptyList(),
                    SettingsDataUIModel(null, null, null, null),
                ),
            )
        val comparisonStateFlow = _comparisonStateFlow.asStateFlow()

        init {
            getAsteroids()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getAsteroids() {
            viewModelScope.launch {
                getFavouritesListUseCase.invoke().collect { asteroids ->
                    _comparisonStateFlow.update {
                        ComparisonViewState(
                            asteroids.map { it.toPagerUiModel() },
                            getSettingsDataUseCase.invoke().first().toSettingsUIModel(),
                        )
                    }
                }
            }
        }
    }
