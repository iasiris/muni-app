package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.OrderHistoryLocalDataSource
import com.iasiris.muniapp.data.local.entity.orderEntityToDomain
import com.iasiris.muniapp.data.remote.datasource.OrderHistoryRemoteDataSource
import com.iasiris.muniapp.data.remote.dto.orderDtoToDomain
import com.iasiris.muniapp.data.remote.dto.orderDtoToEntity
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class OrderHistoryRepositoryImpl @Inject constructor(
    private val remote: OrderHistoryRemoteDataSource,
    private val local: OrderHistoryLocalDataSource
) : OrderHistoryRepository {

    override suspend fun getOrderHistoryByUserId(
        userId: String,
        refreshData: Boolean
    ): List<Order> {
        return if (refreshData) {
            val remoteOrderHistory = remote.getOrderHistoryByUserId(userId)
            local.clearOrderHistory()
            local.insertOrderHistory(remoteOrderHistory.map { it.orderDtoToEntity() })
            remoteOrderHistory.map { it.orderDtoToDomain() }
        } else {
            val localOrderHistory = local.getOrderHistory()
            if (localOrderHistory.isNotEmpty()) {
                localOrderHistory.map { it.orderWithCartItemEntityToDomain() }
            } else {
                val remoteOrderHistory = remote.getOrderHistoryByUserId(userId)
                local.insertOrderHistory(remoteOrderHistory.map { it.orderDtoToEntity() })
                remoteOrderHistory.map { it.orderDtoToDomain() }
            }
        }
    }

    override suspend fun insertOrder(cartItems: List<CartItem>): Order {
        TODO("Not yet implemented")
    }

}