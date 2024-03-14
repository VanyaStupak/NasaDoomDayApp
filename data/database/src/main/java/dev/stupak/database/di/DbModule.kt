package dev.stupak.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.stupak.database.AsteroidsDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    @Singleton
    fun provideDb(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context,
        AsteroidsDB::class.java,
        "asteroids.db",
    ).build()

    @Provides
    @Singleton
    fun provideAsteroidsDao(db: AsteroidsDB) = db.getAsteroidsDao()

    @Provides
    @Singleton
    fun provideFavouritesDao(db: AsteroidsDB) = db.getFavouritesDao()
}
