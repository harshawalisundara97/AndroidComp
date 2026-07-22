package com.androidcomp.app.core.di

import com.androidcomp.app.data.repository.ComponentRepositoryImpl
import com.androidcomp.app.domain.repository.ComponentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindComponentRepository(
        impl: ComponentRepositoryImpl
    ): ComponentRepository
}
