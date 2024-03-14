package dev.stupak.source.impl

import dev.stupak.network.service.AsteroidsApiService
import dev.stupak.source.AsteroidsNetSource
import dev.stupak.source.model.AsteroidsSourceNetModel
import dev.stupak.source.model.toAsteroidsSourceNetModel
import javax.inject.Inject

class AsteroidsNetSourceImpl
    @Inject
    constructor(
        private val asteroidService: AsteroidsApiService,
    ) : AsteroidsNetSource {
        override suspend fun getAsteroidList(
            startDate: String,
            endDate: String,
        ): List<AsteroidsSourceNetModel> =
            apiCall {
                asteroidService.getAsteroids(startDate, endDate)
            }.nearEarthObjects.values.flatten().map { it.toAsteroidsSourceNetModel() }
    }
