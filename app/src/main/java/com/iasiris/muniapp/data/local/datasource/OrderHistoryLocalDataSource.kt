package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.local.entity.OrderItemEntity
import com.iasiris.muniapp.data.local.entity.OrderItemWithProductEntity

interface OrderHistoryLocalDataSource {
    suspend fun insertOrder(order: OrderEntity): OrderEntity
    suspend fun insertOrderItems(
        orderId: Int,
        products: List<OrderItemEntity>
    ): List<OrderItemWithProductEntity>
    suspend fun deleteOrderByOrderId(orderId: Int)
    suspend fun deleteOrderHistory()
    fun getOrderHistory(): List<OrderEntity>?
    fun getOrderItemsWithProductsByOrderId(orderId: Int): List<OrderItemWithProductEntity>
}