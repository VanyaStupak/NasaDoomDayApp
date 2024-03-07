package dev.stupak.repository

import dev.stupak.repository.model.FavouritesRepositoryModel
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun getAsteroidsList(): Flow<List<FavouritesRepositoryModel>>

    suspend fun getAsteroid(id: String): FavouritesRepositoryModel?

    suspend fun insertAsteroid(asteroid: FavouritesRepositoryModel)

    suspend fun deleteAsteroid(id: String)
}