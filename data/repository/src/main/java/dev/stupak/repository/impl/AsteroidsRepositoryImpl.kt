package dev.stupak.repository.impl

import dev.stupak.database.model.AsteroidsDBModel
import dev.stupak.repository.AsteroidsRepository
import dev.stupak.repository.model.AsteroidsRepositoryModel
import dev.stupak.repository.model.toAsteroidsRepositoryModel
import dev.stupak.source.AsteroidsDBSource
import dev.stupak.source.AsteroidsNetSource
import javax.inject.Inject

class AsteroidsRepositoryImpl
@Inject
constructor(
    private val asteroidsDBSource: AsteroidsDBSource,
    private val asteroidsNetSource: AsteroidsNetSource,
): AsteroidsRepository {
    override suspend fun getAsteroidsList(startDate:String, endDate:String): List<AsteroidsRepositoryModel> {
        return asteroidsNetSource.getAsteroidList(startDate, endDate).map {  it.toAsteroidsRepositoryModel() }
    }


    override suspend fun getAsteroid(id: String): AsteroidsRepositoryModel {
       return asteroidsDBSource.getAsteroid(id).toAsteroidsRepositoryModel()
    }

    override suspend fun insertAsteroid(asteroid: AsteroidsDBModel) {
        asteroidsDBSource.insertAsteroid(asteroid)
    }

    override suspend fun deleteAll() {
        asteroidsDBSource.deleteAll()
    }
}