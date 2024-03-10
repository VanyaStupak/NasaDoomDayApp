package dev.stupak.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.stupak.database.model.FavouritesDBModel
import kotlinx.coroutines.flow.Flow
@Dao
interface FavouritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroid(asteroid: FavouritesDBModel)

    @Query("SELECT * FROM favourites WHERE id=:id")
    suspend fun getAsteroid(id: String): FavouritesDBModel?

    @Query("SELECT * FROM favourites")
    fun getAllAsteroids(): Flow<List<FavouritesDBModel>>

    @Query("DELETE FROM favourites WHERE id=:id")
    suspend fun deleteAsteroid(id: String)
}