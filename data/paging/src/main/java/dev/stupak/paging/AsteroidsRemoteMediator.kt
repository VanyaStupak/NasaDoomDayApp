package dev.stupak.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction

import dev.stupak.database.AsteroidsDB
import dev.stupak.database.model.AsteroidsDBModel
import dev.stupak.network.exceptions.UnknownApiException

import dev.stupak.source.AsteroidsNetSource
import dev.stupak.source.model.toAsteroidsDBModel

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPagingApi::class)
class AsteroidsRemoteMediator(
    private val asteroidsNetSource: AsteroidsNetSource,
    private val asteroidsDB: AsteroidsDB,
    private val startDate: Date?,
    private val endDate: Date?,
    private val potentiallyDangerous: Boolean?
) : RemoteMediator<Int, AsteroidsDBModel>() {
    private var pageIndex = 0

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex =
            when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return null
                LoadType.APPEND -> ++pageIndex
            }
        return pageIndex
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AsteroidsDBModel>,
    ): MediatorResult {
        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        val isOneDay = startDate == endDate

        val currentDate = startDate ?: getCurrentDate()
        val limitDate = calculateDate(currentDate, pageIndex)
        val offsetDate = calculateDate(currentDate, pageIndex + 1)
        val limit = formatDate(limitDate)
        val offset = formatDate(offsetDate)
        return try {
            val asteroidList = if (potentiallyDangerous != null)
            {asteroidsNetSource.getAsteroidList(limit, offset)
                .sortedBy { it.closeApproachDate }
                .filter { it.isPotentiallyHazardousAsteroid == potentiallyDangerous }
            } else{
                asteroidsNetSource.getAsteroidList(limit, offset)
                    .sortedBy { it.closeApproachDate }
            }

            asteroidsDB.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        asteroidsDB.getAsteroidsDao().deleteAll()
                    }
                    asteroidList.forEach {
                        asteroidsDB.getAsteroidsDao()
                            .insertAsteroid(it.toAsteroidsDBModel())
                    }
            }
            MediatorResult.Success(
                endOfPaginationReached = asteroidList.isEmpty()
            )
        } catch (exception: UnknownApiException) {
            MediatorResult.Success(endOfPaginationReached = true)
        }
    }

    private fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

    private fun calculateDate(date: Date, daysToAdd: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd)
        return calendar.time
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(date)
    }
}
