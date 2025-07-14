package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.entity.OrderEntity

interface OrderHistoryLocalDataSource {
    suspend fun insertOrder(order: OrderEntity): Boolean
    suspend fun insertOrderHistory(products: List<OrderEntity>)
    suspend fun clearOrderHistory()
    fun getOrderHistory(): List<OrderEntityWithCartItemEntity>
}