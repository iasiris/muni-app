package com.iasiris.muniapp.di

import com.iasiris.muniapp.data.local.datasource.CartItemLocalDataSource
import com.iasiris.muniapp.data.local.datasource.ProductLocalDataSource
import com.iasiris.muniapp.data.remote.datasource.ProductRemoteDataSource
import com.iasiris.muniapp.data.repository.CartItemRepositoryImpl
import com.iasiris.muniapp.data.repository.ProductRepositoryImpl
import com.iasiris.muniapp.domain.repository.CartItemRepository
import com.iasiris.muniapp.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideProductRepository(
        productRemoteDataSource: ProductRemoteDataSource,
        productLocalDataSource: ProductLocalDataSource,
    ): ProductRepository {
        return ProductRepositoryImpl(productRemoteDataSource, productLocalDataSource)
    }

    @Provides
    fun provideCartItemRepository(
        cartItemLocalDataSource: CartItemLocalDataSource
    ): CartItemRepository {
        return CartItemRepositoryImpl(cartItemLocalDataSource)
    }
}