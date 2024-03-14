package dev.stupak.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.stupak.database.model.AsteroidsDBModel

@Dao
interface AsteroidsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroid(asteroids: AsteroidsDBModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(asteroids: List<AsteroidsDBModel>)

    @Query("SELECT * FROM asteroids WHERE id=:id")
    suspend fun getAsteroid(id: String): AsteroidsDBModel

    @Query("SELECT * FROM asteroids")
    fun getAllAsteroids(): PagingSource<Int, AsteroidsDBModel>

    @Query("DELETE FROM asteroids")
    suspend fun deleteAll()
}
