package dev.stupak.source.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.stupak.database.dao.AsteroidsDao
import dev.stupak.database.dao.FavouritesDao
import dev.stupak.network.service.AsteroidsApiService
import dev.stupak.source.AsteroidsDBSource
import dev.stupak.source.AsteroidsNetSource
import dev.stupak.source.FavouritesDBSource
import dev.stupak.source.impl.AsteroidsDBSourceImpl
import dev.stupak.source.impl.AsteroidsNetSourceImpl
import dev.stupak.source.impl.FavouritesDBSourceImpl
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object SourceModule {
    @Provides
    @Singleton
    fun provideAsteroidsNetSource(asteroidsApiService: AsteroidsApiService): AsteroidsNetSource {
        return AsteroidsNetSourceImpl(asteroidsApiService)
    }

    @Provides
    @Singleton
    fun provideAsteroidsDBSource(asteroidsDao:AsteroidsDao): AsteroidsDBSource {
        return AsteroidsDBSourceImpl(asteroidsDao)
    }

    @Provides
    @Singleton
    fun provideFavouritesDBSource(favouritesDao: FavouritesDao): FavouritesDBSource {
        return FavouritesDBSourceImpl(favouritesDao)
    }

}