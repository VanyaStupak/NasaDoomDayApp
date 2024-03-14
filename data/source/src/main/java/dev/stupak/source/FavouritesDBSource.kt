package dev.stupak.source

import dev.stupak.database.model.FavouritesDBModel
import dev.stupak.source.model.FavouritesSourceDBModel
import kotlinx.coroutines.flow.Flow

interface FavouritesDBSource {
    fun getAsteroidsList(): Flow<List<FavouritesSourceDBModel>>

    suspend fun insertAsteroid(favouritesDBModel: FavouritesDBModel)

    suspend fun updateAsteroid(favouritesDBModel: FavouritesDBModel)

    suspend fun getAsteroid(id: String): FavouritesSourceDBModel?

    suspend fun deleteAsteroid(id: String)
}
