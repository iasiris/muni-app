package com.iasiris.muniapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val cartItems: List<CartItemEntity>, //TODO handle list of cart items
    val totalAmount: Double,
    val orderDate: String
)

