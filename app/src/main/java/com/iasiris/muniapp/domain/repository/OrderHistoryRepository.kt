package com.iasiris.muniapp.domain.repository

import com.iasiris.muniapp.domain.model.OrderHistory

interface OrderHistoryRepository {
    fun getOrdersByUserId(userId: String): List<OrderHistory>
    suspend fun insertOrder(orderHistory: OrderHistory)
}