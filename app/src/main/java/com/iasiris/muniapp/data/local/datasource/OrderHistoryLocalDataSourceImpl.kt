package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.dao.OrderHistoryDao
import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.local.entity.OrderItemEntity
import com.iasiris.muniapp.data.local.entity.OrderItemWithProductEntity
import jakarta.inject.Inject

class OrderHistoryLocalDataSourceImpl @Inject constructor(
    private val orderHistoryDao: OrderHistoryDao
) : OrderHistoryLocalDataSource {

    override suspend fun insertOrder( //TODO fix Error inesperado: FOREIGN KEY constraint failed (code 787 SQLITE_CONSTRAINT_FOREIGNKEY)
        order: OrderEntity,
        products: List<OrderItemEntity>
    ): List<OrderItemWithProductEntity> {
        val orderId = orderHistoryDao.insertOrder(order)
        val orderItemsWithOrderId = products.map { it.copy(orderId = orderId.toString()) }
        orderHistoryDao.insertOrderItems(orderItemsWithOrderId)
        return orderHistoryDao.getOrderItemsWithProductsByOrderId(orderId.toInt())
    }

    override suspend fun clearOrderHistory() {
        orderHistoryDao.deleteOrderHistory()
        orderHistoryDao.deleteOrderItems()
    }

    override fun getOrderHistory(): List<OrderEntity> {
        return orderHistoryDao.getOrderHistory()
    }

    override fun getOrderItemsWithProductsByOrderId(orderId: Int): List<OrderItemWithProductEntity> {
        return orderHistoryDao.getOrderItemsWithProductsByOrderId(orderId)
    }
}


