package com.iasiris.muniapp.di

import android.content.Context
import androidx.room.Room
import com.iasiris.muniapp.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext, AppDatabase::class.java, "muniapp_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase) = database.productDao()

    @Provides
    fun provideCartItemDao(database: AppDatabase) = database.cartItemDao()

    @Provides
    fun provideOrderHistoryDao(database: AppDatabase) = database.orderHistoryDao()
}