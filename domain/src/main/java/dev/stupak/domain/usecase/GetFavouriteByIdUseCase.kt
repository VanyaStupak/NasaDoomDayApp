package dev.stupak.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import dev.stupak.domain.model.AsteroidsDomainModel
import dev.stupak.domain.model.toAsteroidsDomainModel
import dev.stupak.repository.AsteroidsRepository
import dev.stupak.repository.FavouritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFavouriteByIdUseCase @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(id: String): AsteroidsDomainModel? {
        return withContext(Dispatchers.IO) {
            favouritesRepository.getAsteroid(id)?.toAsteroidsDomainModel()
        }
    }

}