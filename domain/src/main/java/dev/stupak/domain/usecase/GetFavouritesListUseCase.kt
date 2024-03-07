package dev.stupak.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import dev.stupak.domain.model.AsteroidsDomainModel
import dev.stupak.domain.model.toAsteroidsDomainModel
import dev.stupak.repository.FavouritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFavouritesListUseCase @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(): Flow<List<AsteroidsDomainModel>> {
        return withContext(Dispatchers.IO) {
            favouritesRepository.getAsteroidsList()
                .map { list->
                    list.map { it.toAsteroidsDomainModel() } }
        }
    }

}