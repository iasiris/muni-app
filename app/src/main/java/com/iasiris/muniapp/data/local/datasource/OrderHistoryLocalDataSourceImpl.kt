package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.dao.OrderHistoryDao
import com.iasiris.muniapp.data.local.entity.OrderEntity
import jakarta.inject.Inject

class OrderHistoryLocalDataSourceImpl @Inject constructor(
    private val orderHistoryDao: OrderHistoryDao
) : OrderHistoryLocalDataSource {

    override suspend fun insertOrder(order: OrderEntity): Boolean {
        return orderHistoryDao.insertOrder(order)
    }

    override suspend fun insertOrderHistory(orders: List<OrderEntity>) {
        orderHistoryDao.insertOrderHistory(orders)
    }

    override suspend fun clearOrderHistory() {
        orderHistoryDao.deleteOrderHistory()
    }

    override fun getOrderHistory(): List<OrderEntityWithCartItemEntity> {
        return orderHistoryDao.getOrderHistory()
    }
}


