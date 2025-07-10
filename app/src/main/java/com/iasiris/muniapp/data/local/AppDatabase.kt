package com.iasiris.muniapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iasiris.muniapp.data.local.dao.CartItemDao
import com.iasiris.muniapp.data.local.dao.OrderHistoryDao
import com.iasiris.muniapp.data.local.dao.OrderItemDao
import com.iasiris.muniapp.data.local.dao.ProductDao
import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.local.entity.OrderItemEntity
import com.iasiris.muniapp.data.local.entity.ProductEntity

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun productDao(): ProductDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun orderHistoryDao(): OrderHistoryDao
    abstract fun orderItemDao(): OrderItemDao
}