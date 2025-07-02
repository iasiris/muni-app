package com.iasiris.muniapp.data.local

import com.iasiris.muniapp.data.model.Order

interface OrderDataSource {
    fun getOrdersByUserId(userId: String): List<Order>
    fun updateOrder(order: Order): Boolean
}