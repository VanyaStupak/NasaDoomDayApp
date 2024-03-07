package dev.stupak.source

import androidx.paging.PagingSource
import dev.stupak.database.model.AsteroidsDBModel
import dev.stupak.source.model.AsteroidsSourceDBModel

interface AsteroidsDBSource {
    fun getAsteroidsList(): PagingSource<Int, AsteroidsDBModel>

    suspend fun insertAsteroid(asteroidsDBModel: AsteroidsDBModel)

    suspend fun getAsteroid(id: String): AsteroidsSourceDBModel

    suspend fun deleteAll()
}