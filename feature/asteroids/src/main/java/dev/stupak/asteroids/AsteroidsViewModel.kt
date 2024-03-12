package dev.stupak.asteroids

import android.os.Build
import android.util.Log
import android.view.View
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
import dev.stupak.platform.receiver.MyBroadcastReceiver
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

    data class FilterState(
        val startDateString: String?,
        val endDateString: String?,
        val clearButtonVisibility: Int
    )

    private val _filterStateFlow = MutableStateFlow(FilterState(null, null, View.GONE))
    val filterStateFlow = _filterStateFlow.asStateFlow()

    private var isInternetConnected = false

    fun setFilterState(startDate: String?, endDate: String?, visibility: Int) {
        _filterStateFlow.update { FilterState(startDate, endDate, visibility) }
    }

    private val _asteroidsListStateFlow =
        MutableStateFlow<PagingData<AsteroidsUIModel>>(PagingData.empty())
    val assetsListStateFlow = _asteroidsListStateFlow.asStateFlow()

    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded = _isDataLoaded.asStateFlow()

    private val _connectionStateFlow = MutableStateFlow(false)
    val connectionStateFlow = _connectionStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            MyBroadcastReceiver.isOnline.collect { isConnected ->
                isInternetConnected = isConnected
                getAsteroids(null, null, null)
                _connectionStateFlow.update { isConnected }
            }
        }
    }

    fun setSort(start: Date?, end: Date?, isPotentiallyDangerous: Boolean?) {
        getAsteroids(start, end, isPotentiallyDangerous)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAsteroids(startDate: Date?, endDate: Date?, isPotentiallyDangerous: Boolean?) {
        viewModelScope.launch {
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