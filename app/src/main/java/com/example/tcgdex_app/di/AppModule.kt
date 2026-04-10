package com.example.tcgdex_app.di

import com.example.tcgdex_app.data.remote.datasource.RemoteDataSource
import com.example.tcgdex_app.data.remote.datasource.RemoteDataSourceImpl
import com.example.tcgdex_app.data.repository.TCGDexRepositoryImpl
import com.example.tcgdex_app.domain.TCGDexRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindTCGDexRepository(
        impl: TCGDexRepositoryImpl,
    ): TCGDexRepository

    @Binds
    @Singleton
    abstract fun bindCardRemoteDataSource(
        impl: RemoteDataSourceImpl
    ): RemoteDataSource

    companion object {
        @Provides
        @Singleton
        fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
    }
}