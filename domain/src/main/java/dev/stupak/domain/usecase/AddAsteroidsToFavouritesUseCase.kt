package dev.stupak.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import dev.stupak.repository.AsteroidsRepository
import dev.stupak.repository.FavouritesRepository
import dev.stupak.repository.model.toFavouritesDBModel
import dev.stupak.repository.model.toFavouritesRepositoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddAsteroidsToFavouritesUseCase @Inject constructor(
    private val favouritesRepository: FavouritesRepository,
    private val asteroidsRepository: AsteroidsRepository
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(id: String) {
        return withContext(Dispatchers.IO) {
            val asteroid = asteroidsRepository.getAsteroid(id)
            favouritesRepository.insertAsteroid(asteroid.toFavouritesRepositoryModel())
        }
    }

}