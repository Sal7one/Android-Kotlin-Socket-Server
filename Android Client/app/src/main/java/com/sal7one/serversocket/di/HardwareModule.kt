package com.sal7one.serversocket.di

import android.content.Context
import android.hardware.SensorManager
import com.sal7one.serversocket.data.AppSensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object HardwareModule {

    @Singleton
    @Provides
    fun provideSensorManager(@ApplicationContext appContext: Context): AppSensorManager {
        return AppSensorManager(appContext)
    }
}