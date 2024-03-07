package dev.stupak.source

import dev.stupak.source.model.AsteroidsSourceNetModel

interface AsteroidsNetSource: BaseNetSource {
   suspend fun getAsteroidList(
        startDate: String, endDate: String
    ): List<AsteroidsSourceNetModel>

}