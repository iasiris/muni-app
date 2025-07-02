package com.iasiris.muniapp

import com.iasiris.muniapp.data.local.OrderDataSource
import com.iasiris.muniapp.data.local.OrderDataSourceImpl
import com.iasiris.muniapp.data.local.ProductDataSource
import com.iasiris.muniapp.data.local.ProductDataSourceImpl
import com.iasiris.muniapp.data.local.UserDataSource
import com.iasiris.muniapp.data.local.UserDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
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