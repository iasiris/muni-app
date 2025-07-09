package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.domain.model.Order

interface OrderDataSource {
    fun getOrdersByUserId(userId: String): List<Order>
    fun updateOrder(order: Order): Boolean
}