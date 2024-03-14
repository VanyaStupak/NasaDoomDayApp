package dev.stupak.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.stupak.repository.AsteroidsRepository
import dev.stupak.repository.FavouritesRepository
import dev.stupak.repository.impl.AsteroidsRepositoryImpl
import dev.stupak.repository.impl.FavouritesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindAsteroidsRepository(impl: AsteroidsRepositoryImpl): AsteroidsRepository

    @Singleton
    @Binds
    abstract fun bindFavouritesRepository(impl: FavouritesRepositoryImpl): FavouritesRepository
}
