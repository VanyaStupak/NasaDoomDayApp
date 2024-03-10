package dev.stupak.details

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stupak.domain.usecase.AddAsteroidsToFavouritesUseCase
import dev.stupak.domain.usecase.GetAsteroidByIdUseCase
import dev.stupak.details.model.AsteroidsDetailsUIModel
import dev.stupak.details.model.toAsteroidsDetailsUiModel
import dev.stupak.domain.usecase.GetFavouriteByIdUseCase
import dev.stupak.domain.usecase.GetFavouritesListUseCase
import dev.stupak.domain.usecase.RemoveAsteroidFromFavouritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAsteroidByIdUseCase: GetAsteroidByIdUseCase,
    private val addAsteroidsToFavouritesUseCase: AddAsteroidsToFavouritesUseCase,
    private val removeAsteroidFromFavouritesUseCase: RemoveAsteroidFromFavouritesUseCase,
    private val getFavouriteByIdUseCase: GetFavouriteByIdUseCase,
    private val getFavouritesListUseCase: GetFavouritesListUseCase
) : ViewModel() {

    data class DetailsViewState(
        val asteroid: AsteroidsDetailsUIModel?,
        val isInFavourite: Boolean
    )

    private val _detailsData = MutableStateFlow(DetailsViewState(null,false))
    val detailsData = _detailsData.asStateFlow()


    fun getFavouriteAsteroid(
        asteroidId: String?
    ){
        viewModelScope.launch {
            getFavourite(asteroidId = asteroidId.toString())
        }
    }

    private suspend fun getFavourite(asteroidId: String) {
       val favourite = getFavouriteByIdUseCase.invoke(asteroidId)
        _detailsData.update { DetailsViewState(null,favourite != null)}
    }

    suspend fun getFavouritesSize(): Int {
        return getFavouritesListUseCase.invoke().first().size
    }

    fun getAsteroidInfo(
        asteroidId: String?,
        fromFragment: String?
    ) {
        viewModelScope.launch {
            getAsteroid(asteroidId = asteroidId.toString(), fromFragment = fromFragment.toString())
        }
    }


    fun addAsteroidToFavourites(
        asteroidId: String?,
    ) {
        viewModelScope.launch {
            addAsteroid(asteroidId = asteroidId.toString())
        }
    }


    fun removeAsteroidFromFavourites(
        asteroidId: String?,
    ) {
        viewModelScope.launch {
            removeAsteroid(asteroidId = asteroidId.toString())
        }
    }


    private suspend fun addAsteroid(asteroidId: String) {
        addAsteroidsToFavouritesUseCase.invoke(id = asteroidId)
    }


    private suspend fun getAsteroid(
        asteroidId: String,
        fromFragment: String?
    ) {
        val asteroid = if (fromFragment == "favourites" || fromFragment == "comparison" ){
            getFavouriteByIdUseCase.invoke(asteroidId)
        } else {
            getAsteroidByIdUseCase.invoke(asteroidId)
        }
        _detailsData.update { DetailsViewState(asteroid?.toAsteroidsDetailsUiModel(),_detailsData.value.isInFavourite) }
    }


    private suspend fun removeAsteroid(asteroidId: String) {
        removeAsteroidFromFavouritesUseCase.invoke(asteroidId)
    }
}