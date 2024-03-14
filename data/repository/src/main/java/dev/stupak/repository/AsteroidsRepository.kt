package dev.stupak.repository

import dev.stupak.database.model.AsteroidsDBModel
import dev.stupak.repository.model.AsteroidsRepositoryModel

interface AsteroidsRepository {
    suspend fun getAsteroidsList(
        startDate: String,
        endDate: String,
    ): List<AsteroidsRepositoryModel>

    suspend fun getAsteroid(id: String): AsteroidsRepositoryModel

    suspend fun insertAsteroid(asteroid: AsteroidsDBModel)

    suspend fun deleteAll()
}
