package com.iasiris.muniapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iasiris.muniapp.data.database.dao.CartItemDao
import com.iasiris.muniapp.data.database.dao.OrderHistoryDao
import com.iasiris.muniapp.data.database.dao.OrderItemDao
import com.iasiris.muniapp.data.database.dao.ProductDao
import com.iasiris.muniapp.data.database.entity.CartItemEntity
import com.iasiris.muniapp.data.database.entity.OrderEntity
import com.iasiris.muniapp.data.database.entity.OrderItemEntity
import com.iasiris.muniapp.data.database.entity.ProductEntity


@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun productDao(): ProductDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun orderHistoryDao(): OrderHistoryDao
    abstract fun orderItemDao(): OrderItemDao
}