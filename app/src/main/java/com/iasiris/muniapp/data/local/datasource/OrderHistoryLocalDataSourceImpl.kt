package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.dao.OrderHistoryDao
import com.iasiris.muniapp.domain.model.OrderHistory
import jakarta.inject.Inject

class OrderHistoryLocalDataSourceImpl @Inject constructor(
    private val orderHistoryDao: OrderHistoryDao
) : OrderHistoryLocalDataSource {
    override fun getOrdersByUserId(userId: String): List<OrderHistory> {
        TODO("Not yet implemented")
    }

    override fun updateOrder(orderHistory: OrderHistory): Boolean {
        TODO("Not yet implemented")
    }
}


