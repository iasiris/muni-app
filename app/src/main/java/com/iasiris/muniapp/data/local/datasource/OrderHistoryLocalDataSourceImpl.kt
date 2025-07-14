package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.dao.OrderHistoryDao
import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.local.entity.OrderItemEntity
import com.iasiris.muniapp.data.local.entity.OrderItemWithProductEntity
import jakarta.inject.Inject

class OrderHistoryLocalDataSourceImpl @Inject constructor(
    private val orderHistoryDao: OrderHistoryDao
) : OrderHistoryLocalDataSource {

    override suspend fun insertOrder(
        order: OrderEntity,
        products: List<OrderItemEntity>
    ): List<OrderItemWithProductEntity> {
        orderHistoryDao.insertOrder(order)
        orderHistoryDao.insertOrderItems(products)
        return orderHistoryDao.getOrderItemsWithProductsByOrderId(order.id)
    }

    override suspend fun clearOrderHistory() {
        orderHistoryDao.deleteOrderHistory()
    }

    override fun getOrderHistory(): List<OrderEntity> {
        return orderHistoryDao.getOrderHistory()
    }
}


