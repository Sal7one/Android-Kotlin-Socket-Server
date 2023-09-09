package com.sal7one.serversocket.di

import android.content.Context
import com.sal7one.serversocket.data.AppSensorManager
import com.sal7one.serversocket.data.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkManager(@ApplicationContext appContext: Context): NetworkManager {
        return NetworkManager(appContext)
    }
}