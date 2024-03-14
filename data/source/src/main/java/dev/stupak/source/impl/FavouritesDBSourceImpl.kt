package dev.stupak.source.impl

import dev.stupak.database.dao.FavouritesDao
import dev.stupak.database.model.FavouritesDBModel
import dev.stupak.source.FavouritesDBSource
import dev.stupak.source.model.FavouritesSourceDBModel
import dev.stupak.source.model.toFavouritesSourceDBModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouritesDBSourceImpl
    @Inject
    constructor(
        private val favouritesDao: FavouritesDao,
    ) : FavouritesDBSource {
        override fun getAsteroidsList(): Flow<List<FavouritesSourceDBModel>> {
            return favouritesDao.getAllAsteroids().map { list ->
                list.map { favouritesDBModel ->
                    favouritesDBModel.toFavouritesSourceDBModel()
                }
            }
        }

        override suspend fun insertAsteroid(favouritesDBModel: FavouritesDBModel) {
            favouritesDao.insertAsteroid(favouritesDBModel)
        }

        override suspend fun updateAsteroid(favouritesDBModel: FavouritesDBModel) {
            favouritesDao.updateAsteroid(favouritesDBModel)
        }

        override suspend fun getAsteroid(id: String): FavouritesSourceDBModel?  {
            return favouritesDao.getAsteroid(id)?.toFavouritesSourceDBModel()
        }

        override suspend fun deleteAsteroid(id: String) {
            favouritesDao.deleteAsteroid(id)
        }
    }
