package dev.stupak.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.stupak.database.dao.AsteroidsDao
import dev.stupak.database.dao.FavouritesDao
import dev.stupak.database.model.AsteroidsDBModel
import dev.stupak.database.model.FavouritesDBModel

@Database(
    entities = [AsteroidsDBModel::class, FavouritesDBModel::class],
    version = 1,
    exportSchema = true,
)
abstract class AsteroidsDB : RoomDatabase() {
    abstract fun getAsteroidsDao(): AsteroidsDao

    abstract fun getFavouritesDao(): FavouritesDao
}
