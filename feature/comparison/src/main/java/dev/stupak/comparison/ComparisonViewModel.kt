package dev.stupak.comparison

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stupak.comparison.model.PagerUIModel
import dev.stupak.comparison.model.toPagerUiModel
import dev.stupak.domain.usecase.GetFavouritesListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ComparisonViewModel @Inject constructor(
    private val getFavouritesListUseCase: GetFavouritesListUseCase
): ViewModel() {

    private val _favouritesListStateFlow = MutableStateFlow<List<PagerUIModel>>(emptyList())
    val favouritesListStateFlow = _favouritesListStateFlow.asStateFlow()

    init {
        getAsteroids()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAsteroids() {
        viewModelScope.launch {
            getFavouritesListUseCase.invoke().collect { asteroids ->
                _favouritesListStateFlow.update { asteroids.map { it.toPagerUiModel() } }
            }
        }
    }
}