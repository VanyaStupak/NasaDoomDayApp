package dev.stupak.asteroids

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stupak.domain.usecase.GetAsteroidsPagingDataUseCase
import dev.stupak.model.AsteroidsUIModel
import dev.stupak.model.toAsteroidsUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AsteroidsViewModel
@Inject
constructor(
    private val getAsteroidsPagingDataUseCase: GetAsteroidsPagingDataUseCase
) : ViewModel() {

    fun setSort(start: Date?, end: Date?, isPotentiallyDangerous: Boolean?) {
        getAsteroids(start, end, isPotentiallyDangerous)
    }


    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded = _isDataLoaded.asStateFlow()

    init {
        getAsteroids(null, null, null)
    }

    private val _asteroidsListStateFlow =
        MutableStateFlow<PagingData<AsteroidsUIModel>>(PagingData.empty())
    val assetsListStateFlow = _asteroidsListStateFlow.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAsteroids(startDate: Date?, endDate: Date?, isPotentiallyDangerous: Boolean?) {
        viewModelScope.launch(Dispatchers.IO) {
            _isDataLoaded.value = true
            getAsteroidsPagingDataUseCase.invoke(startDate, endDate, isPotentiallyDangerous)
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _asteroidsListStateFlow.update {
                        pagingData.map { it.toAsteroidsUiModel() }
                    }
                }

        }
    }
}