package com.acous.airqualityindex.di

import android.content.Context
import com.acous.airqualityindex.data.AppDatabase
import com.acous.airqualityindex.data.AqiDao
import com.acous.airqualityindex.data.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModules {


    @Provides
    fun provideRepository(aqiDao: AqiDao): Repository = Repository(aqiDao)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideApodDao(db: AppDatabase) = db.aqiDao()

}