package com.iasiris.muniapp.di

import com.iasiris.muniapp.data.repository.OrderDataSource
import com.iasiris.muniapp.data.repository.OrderDataSourceImpl
import com.iasiris.muniapp.data.repository.ProductDataSource
import com.iasiris.muniapp.data.repository.ProductDataSourceImpl
import com.iasiris.muniapp.data.repository.UserDataSource
import com.iasiris.muniapp.data.repository.UserDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun provideProductDataSource(): ProductDataSource {
        return ProductDataSourceImpl()
    }

    @Provides
    fun provideUserDataSource(): UserDataSource {
        return UserDataSourceImpl()
    }

    @Provides
    fun provideOrderDataSource(): OrderDataSource {
        return OrderDataSourceImpl()
    }
}