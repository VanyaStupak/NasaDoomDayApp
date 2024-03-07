package dev.stupak.favourites

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stupak.domain.usecase.GetFavouritesListUseCase
import dev.stupak.favourites.model.FavouritesUIModel
import dev.stupak.favourites.model.toFavouritesUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavouritesListUseCase: GetFavouritesListUseCase
) : ViewModel() {

    private val _asteroidsListStateFlow = MutableStateFlow<List<FavouritesUIModel>>(emptyList())
    val asteroidsListStateFlow = _asteroidsListStateFlow.asStateFlow()

    init {
        getAsteroids()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAsteroids() {
        viewModelScope.launch {
            getFavouritesListUseCase.invoke().collect { asteroids ->
                _asteroidsListStateFlow.update { asteroids.map { it.toFavouritesUiModel() } }
            }

        }
    }

}