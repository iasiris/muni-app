package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.OrderHistoryLocalDataSource
import com.iasiris.muniapp.data.local.entity.productEntityToDomain
import com.iasiris.muniapp.data.remote.datasource.OrderHistoryRemoteDataSource
import com.iasiris.muniapp.data.remote.dto.productDtoToDomain
import com.iasiris.muniapp.data.remote.dto.productDtoToEntity
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class OrderHistoryRepositoryImpl @Inject constructor(
    private val local: OrderHistoryLocalDataSource,
    private val remote: OrderHistoryRemoteDataSource
) : OrderHistoryRepository {

    override fun getOrderHistoryByUserId(userId: String, refreshData: Boolean): List<Order> {
        return if (refreshData) {
            val remoteProducts = remote.getOrdersByUserId(userId)
            local.clearProducts()
            local.insertProducts(remoteProducts.map { it.productDtoToEntity() })
            remoteProducts.map { it.productDtoToDomain() }
        } else {
            val localProducts = local.getProducts()
            if (localProducts.isNotEmpty()) {
                localProducts.map { it.productEntityToDomain() }
            } else {
                val remoteProducts = remote.getProducts()
                local.insertProducts(remoteProducts.map { it.productDtoToEntity() })
                remoteProducts.map { it.productDtoToDomain() }
            }
        }
    }

    override suspend fun insertOrder(cartItems: List<CartItem>): Order {
        TODO("Not yet implemented")
    }

}