package dev.stupak.repository

import androidx.paging.PagingData
import dev.stupak.database.model.AsteroidsDBModel
import dev.stupak.repository.model.AsteroidsRepositoryModel
import kotlinx.coroutines.flow.Flow


interface AsteroidsRepository {

    suspend fun getAsteroidsList(startDate: String, endDate:String): List<AsteroidsRepositoryModel>

    suspend fun getAsteroid(id: String): AsteroidsRepositoryModel

    suspend fun insertAsteroid(asteroid: AsteroidsDBModel)

    suspend fun deleteAll()
}