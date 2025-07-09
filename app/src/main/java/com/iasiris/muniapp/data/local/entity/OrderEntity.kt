package com.iasiris.muniapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, //TODO no deberia ser int?
    val orderDate: Long = System.currentTimeMillis(),
    val totalAmount: Double,
    val totalItems: Int
)

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val orderItemId: Int = 0,
    var orderId: Int = 0,
    val productId: String,
    val quantity: Int,
    val price: Double
)