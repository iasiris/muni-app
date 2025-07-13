package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.dao.OrderHistoryDao
import com.iasiris.muniapp.domain.model.Order
import jakarta.inject.Inject

class OrderHistoryLocalDataSourceImpl @Inject constructor(
    private val orderHistoryDao: OrderHistoryDao
) : OrderHistoryLocalDataSource {
    override fun getOrderHistoryByUserId(userId: String): List<Order> {
        TODO("Not yet implemented")
    }

    override fun updateOrder(order: Order): Boolean {
        TODO("Not yet implemented")
    }
}


