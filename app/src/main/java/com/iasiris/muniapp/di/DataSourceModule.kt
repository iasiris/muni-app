package com.iasiris.muniapp.di

import com.iasiris.muniapp.data.local.dao.CartItemDao
import com.iasiris.muniapp.data.local.dao.ProductDao
import com.iasiris.muniapp.data.local.datasource.CartItemLocalDataSource
import com.iasiris.muniapp.data.local.datasource.CartItemLocalDataSourceImpl
import com.iasiris.muniapp.data.local.datasource.OrderDataSource
import com.iasiris.muniapp.data.local.datasource.OrderDataSourceImpl
import com.iasiris.muniapp.data.local.datasource.ProductLocalDataSource
import com.iasiris.muniapp.data.local.datasource.ProductLocalDataSourceImpl
import com.iasiris.muniapp.data.remote.datasource.UserDataSource
import com.iasiris.muniapp.data.remote.datasource.UserDataSourceImpl
import com.iasiris.muniapp.data.remote.ProductApiService
import com.iasiris.muniapp.data.remote.datasource.ProductRemoteDataSource
import com.iasiris.muniapp.data.remote.datasource.ProductRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun provideProductLocalDataSource(productDao: ProductDao): ProductLocalDataSource {
        return ProductLocalDataSourceImpl(productDao)
    }

    @Provides
    fun provideProductRemoteDataSource(productApiService: ProductApiService): ProductRemoteDataSource {
        return ProductRemoteDataSourceImpl(productApiService)
    }

    @Provides
    fun provideCartItemLocalDataSource(cartItemDao: CartItemDao): CartItemLocalDataSource {
        return CartItemLocalDataSourceImpl(cartItemDao)
    }

    @Provides
    fun provideOrderDataSource(): OrderDataSource {
        return OrderDataSourceImpl()
    }

    @Provides //TODO delete this
    fun provideUserDataSource(): UserDataSource {
        return UserDataSourceImpl()
    }
}