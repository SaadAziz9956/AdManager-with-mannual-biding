package com.example.admodule.di

import com.example.admodule.data.repository.AdMobRepositoryImpl
import com.example.admodule.data.repository.AdsRepositoryImpl
import com.example.admodule.data.repository.FacebookAdsRepositoryImpl
import com.example.admodule.domain.repository.AdMobRepository
import com.example.admodule.domain.repository.AdsRepository
import com.example.admodule.domain.repository.FacebookAdsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAdsRepository(
        adsRepositoryImpl: AdsRepositoryImpl
    ): AdsRepository

    @Binds
    @Singleton
    abstract fun bindAdMobRepository(
        adMobRepositoryImpl: AdMobRepositoryImpl
    ): AdMobRepository

    @Binds
    @Singleton
    abstract fun bindFbAdRepository(
        fbAdsRepositoryImpl: FacebookAdsRepositoryImpl
    ): FacebookAdsRepository


}