package dev.stupak.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.stupak.database.AsteroidsDB
import dev.stupak.domain.model.AsteroidsDomainModel
import dev.stupak.domain.model.toAsteroidsDomainModel
import dev.stupak.paging.AsteroidsRemoteMediator
import dev.stupak.repository.model.toAsteroidsRepositoryModel
import dev.stupak.source.AsteroidsNetSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class GetAsteroidsPagingDataUseCase @Inject constructor(
    private val asteroidsNetSource: AsteroidsNetSource,
    private val asteroidsDB: AsteroidsDB
) {

    @OptIn(ExperimentalPagingApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(startDate: Date?, endDate: Date?,isPotentiallyDangerous: Boolean?): Flow<PagingData<AsteroidsDomainModel>> {
        return withContext(Dispatchers.IO) {
            Pager(
                PagingConfig(pageSize = 1),
                remoteMediator = AsteroidsRemoteMediator(
                    asteroidsNetSource,
                    asteroidsDB,
                    startDate,
                    endDate,
                    isPotentiallyDangerous
                ),
                pagingSourceFactory = { asteroidsDB.getAsteroidsDao().getAllAsteroids() }
            ).flow
                .map { pagingData ->
                    pagingData.map {
                        it.toAsteroidsRepositoryModel().toAsteroidsDomainModel()
                    }
                }
        }
    }

}