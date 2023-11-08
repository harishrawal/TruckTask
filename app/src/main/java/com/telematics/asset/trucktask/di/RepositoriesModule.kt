package com.telematics.asset.trucktask.di

import com.telematics.asset.trucktask.repository.MainRepository
import com.telematics.asset.trucktask.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoriesModule {
    @Binds
    fun mainRepository(mainRepositoryImpl: MainRepositoryImpl) : MainRepository

}

