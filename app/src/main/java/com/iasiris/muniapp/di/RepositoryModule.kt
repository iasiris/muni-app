package com.iasiris.muniapp.di

import com.iasiris.muniapp.data.local.datasource.CartItemLocalDataSource
import com.iasiris.muniapp.data.local.datasource.OrderHistoryLocalDataSource
import com.iasiris.muniapp.data.local.datasource.ProductLocalDataSource
import com.iasiris.muniapp.data.remote.datasource.OrderHistoryRemoteDataSource
import com.iasiris.muniapp.data.remote.datasource.ProductRemoteDataSource
import com.iasiris.muniapp.data.remote.datasource.UserRemoteDataSource
import com.iasiris.muniapp.data.repository.CartItemRepositoryImpl
import com.iasiris.muniapp.data.repository.OrderHistoryRepositoryImpl
import com.iasiris.muniapp.data.repository.ProductRepositoryImpl
import com.iasiris.muniapp.data.repository.UserRepositoryImpl
import com.iasiris.muniapp.domain.repository.CartItemRepository
import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import com.iasiris.muniapp.domain.repository.ProductRepository
import com.iasiris.muniapp.domain.repository.UserRepository
import com.iasiris.muniapp.utils.CommonUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource
    ): UserRepository {
        return UserRepositoryImpl(userRemoteDataSource)
    }

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

    @Provides
    fun provideOrderRepository(
        orderHistoryRemoteDataSource: OrderHistoryRemoteDataSource,
        orderHistoryLocalDataSource: OrderHistoryLocalDataSource,
        commonUtils: CommonUtils
    ): OrderHistoryRepository {
        return OrderHistoryRepositoryImpl(
            orderHistoryRemoteDataSource,
            orderHistoryLocalDataSource,
            commonUtils
        )
    }
}