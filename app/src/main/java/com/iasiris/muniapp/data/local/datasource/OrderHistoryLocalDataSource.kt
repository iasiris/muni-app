package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.domain.model.OrderHistory

interface OrderHistoryLocalDataSource {
    fun getOrdersByUserId(userId: String): List<OrderHistory>
    fun updateOrder(orderHistory: OrderHistory): Boolean
}