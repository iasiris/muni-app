package com.iasiris.muniapp.di

import com.iasiris.muniapp.data.local.dao.CartItemDao
import com.iasiris.muniapp.data.local.dao.OrderHistoryDao
import com.iasiris.muniapp.data.local.dao.ProductDao
import com.iasiris.muniapp.data.local.datasource.CartItemLocalDataSource
import com.iasiris.muniapp.data.local.datasource.CartItemLocalDataSourceImpl
import com.iasiris.muniapp.data.local.datasource.OrderHistoryLocalDataSource
import com.iasiris.muniapp.data.local.datasource.OrderHistoryLocalDataSourceImpl
import com.iasiris.muniapp.data.local.datasource.ProductLocalDataSource
import com.iasiris.muniapp.data.local.datasource.ProductLocalDataSourceImpl
import com.iasiris.muniapp.data.remote.OrderHistoryApiService
import com.iasiris.muniapp.data.remote.ProductApiService
import com.iasiris.muniapp.data.remote.datasource.OrderHistoryRemoteDataSource
import com.iasiris.muniapp.data.remote.datasource.OrderHistoryRemoteDataSourceImpl
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
    fun provideOrderHistoryLocalDataSource(orderHistoryDao: OrderHistoryDao): OrderHistoryLocalDataSource {
        return OrderHistoryLocalDataSourceImpl(orderHistoryDao)
    }

    @Provides
    fun provideOrderHistoryRemoteDataSource(orderHistoryApiService: OrderHistoryApiService): OrderHistoryRemoteDataSource {
        return OrderHistoryRemoteDataSourceImpl(orderHistoryApiService)
    }
}