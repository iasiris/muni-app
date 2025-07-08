package com.iasiris.muniapp.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CartItemWithProductEntity(
    @Embedded val cartItem: CartItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity
)