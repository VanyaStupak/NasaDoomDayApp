package dev.stupak.repository.impl

import dev.stupak.database.AsteroidsDB
import dev.stupak.database.model.FavouritesDBModel
import dev.stupak.repository.FavouritesRepository
import dev.stupak.repository.model.FavouritesRepositoryModel
import dev.stupak.repository.model.toFavouritesDBModel
import dev.stupak.repository.model.toFavouritesRepositoryModel
import dev.stupak.source.AsteroidsDBSource
import dev.stupak.source.AsteroidsNetSource
import dev.stupak.source.FavouritesDBSource
import dev.stupak.source.model.toFavouritesSourceDBModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouritesRepositoryImpl
@Inject
constructor(
    private val favouritesDBSource: FavouritesDBSource,
): FavouritesRepository {
    override fun getAsteroidsList(): Flow<List<FavouritesRepositoryModel>> {
        return favouritesDBSource.getAsteroidsList().map { list ->
            list.map { favouritesDBModel ->
                favouritesDBModel.toFavouritesRepositoryModel()
            }
        }
    }

    override suspend fun getAsteroid(id: String): FavouritesRepositoryModel? {
        return favouritesDBSource.getAsteroid(id)?.toFavouritesRepositoryModel()
    }

    override suspend fun insertAsteroid(asteroid: FavouritesRepositoryModel) {
       favouritesDBSource.insertAsteroid(asteroid.toFavouritesDBModel())
    }

    override suspend fun deleteAsteroid(id: String) {
        favouritesDBSource.deleteAsteroid(id)
    }
}