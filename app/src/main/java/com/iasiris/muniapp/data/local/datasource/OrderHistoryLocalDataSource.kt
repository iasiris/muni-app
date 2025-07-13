package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.domain.model.Order

interface OrderHistoryLocalDataSource {
    fun getOrderHistoryByUserId(userId: String): List<Order>
    fun updateOrder(order: Order): Boolean
}