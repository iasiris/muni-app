package com.iasiris.muniapp.di

import com.iasiris.muniapp.utils.CommonUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonUtilsModule {
    @Provides
    @Singleton
    fun provideCommonUtils(): CommonUtils {
        return CommonUtils()
    }
}