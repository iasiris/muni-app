package com.iasiris.muniapp.domain.repository

import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order

interface OrderHistoryRepository {
    suspend fun getOrderHistoryByUserId(userId: String, refreshData: Boolean): List<Order>
    suspend fun insertOrder(cartItems: List<CartItem>): Order
}