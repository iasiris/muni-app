package com.iasiris.muniapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iasiris.muniapp.data.local.entity.OrderEntity

@Dao
interface OrderHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderHistory(products: List<OrderEntity>)

    @Query("DELETE FROM orders")
    suspend fun deleteOrderHistory()

    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getOrderHistory(): List<OrderEntity>
}