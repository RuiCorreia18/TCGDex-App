package com.example.tcgdex_app.di

import com.example.tcgdex_app.data.repository.TCGDexRepositoryImpl
import com.example.tcgdex_app.domain.TCGDexRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindTCGDexRepository(
        impl: TCGDexRepositoryImpl,
    ): TCGDexRepository
}