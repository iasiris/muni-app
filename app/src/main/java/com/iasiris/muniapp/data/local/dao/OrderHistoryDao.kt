package com.iasiris.muniapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.local.entity.OrderItemEntity
import com.iasiris.muniapp.data.local.entity.OrderItemWithProductEntity

@Dao
interface OrderHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(products: List<OrderItemEntity>)

    @Query("DELETE FROM orders")
    suspend fun deleteOrderHistory()

    @Query("DELETE FROM order_items")
    suspend fun deleteOrderItems()

    @Query("SELECT * FROM orders ORDER BY orderDate ASC")
    fun getOrderHistory(): List<OrderEntity>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItemsWithProductsByOrderId(orderId: Int): List<OrderItemWithProductEntity>
}