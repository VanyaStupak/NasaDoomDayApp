package dev.stupak.local.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.stupak.local.DataStoreManager
import dev.stupak.local.impl.DataStoreManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStoreManager(
        @ApplicationContext context: Context,
    ): DataStoreManager {
        return DataStoreManagerImpl(context)
    }
}