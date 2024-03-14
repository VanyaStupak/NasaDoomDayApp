package dev.stupak.source.impl

import androidx.paging.PagingSource
import dev.stupak.database.dao.AsteroidsDao
import dev.stupak.database.model.AsteroidsDBModel
import dev.stupak.source.AsteroidsDBSource
import dev.stupak.source.model.AsteroidsSourceDBModel
import dev.stupak.source.model.toAsteroidsSourceDBModel
import javax.inject.Inject

class AsteroidsDBSourceImpl
    @Inject
    constructor(
        private val asteroidDao: AsteroidsDao,
    ) : AsteroidsDBSource {
        override fun getAsteroidsList(): PagingSource<Int, AsteroidsDBModel> {
            return asteroidDao.getAllAsteroids()
        }

        override suspend fun insertAsteroid(asteroidsDBModel: AsteroidsDBModel) {
            asteroidDao.insertAsteroid(asteroidsDBModel)
        }

        override suspend fun getAsteroid(id: String): AsteroidsSourceDBModel {
            return asteroidDao.getAsteroid(id).toAsteroidsSourceDBModel()
        }

        override suspend fun deleteAll() {
            asteroidDao.deleteAll()
        }
    }
