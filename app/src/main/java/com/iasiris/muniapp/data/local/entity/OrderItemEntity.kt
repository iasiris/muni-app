package com.iasiris.muniapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

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
    ],
    indices = [Index(value = ["orderId"]), Index(value = ["productId"])]
)

data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var orderId: String,
    val productId: String,
    val quantity: Int
)


data class OrderItemWithProductEntity(
    @Embedded val orderItem: OrderItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity
)