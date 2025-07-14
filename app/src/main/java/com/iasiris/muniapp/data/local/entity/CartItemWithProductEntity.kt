package com.iasiris.muniapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.iasiris.muniapp.domain.model.CartItem

data class CartItemWithProductEntity(
    @Embedded val cartItem: CartItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity
)

fun CartItemWithProductEntity.cartItemWithProductEntityToDomain(): CartItem {
    return CartItem(
        id = cartItem.id.toString(),
        product = product.productEntityToDomain(),
        quantity = cartItem.quantity
    )
}