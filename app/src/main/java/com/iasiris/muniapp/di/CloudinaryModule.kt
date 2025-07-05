package com.iasiris.muniapp.di

import com.cloudinary.Cloudinary
import com.iasiris.muniapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudinaryModule {
    @Provides
    @Singleton
    fun provideCloudinary(): Cloudinary {
        return Cloudinary(
            mapOf(
                "cloud_name" to BuildConfig.CLOUDINARY_CLOUD_NAME,
                "api_key" to BuildConfig.CLOUDINARY_API_KEY,
                "api_secret" to BuildConfig.CLOUDINARY_API_SECRET,
                "upload_preset" to BuildConfig.CLOUDINARY_UPLOAD_PRESET
            )
        )
    }

}