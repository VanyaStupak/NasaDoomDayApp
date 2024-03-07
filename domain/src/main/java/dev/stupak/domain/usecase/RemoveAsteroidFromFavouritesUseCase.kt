package dev.stupak.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import dev.stupak.repository.FavouritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveAsteroidFromFavouritesUseCase @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(id:String){
        return withContext(Dispatchers.IO) {
            favouritesRepository.deleteAsteroid(id)
        }
    }

}