package com.iasiris.muniapp.domain.mapper

import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.data.local.entity.CartItemWithProductEntity
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Product

fun CartItem.cartItemToEntity() = CartItemEntity(id.toInt(), product.id, quantity)

fun CartItemEntity.cartItemEntityToDomain() = CartItem(
    id = id.toString(),
    product = Product(id = productId, "", "", "", 0.0, false, ""),
    quantity = quantity
)

fun CartItemWithProductEntity.cartItemWithProductEntityToDomain(): CartItem {
    return CartItem(
        id = cartItem.id.toString(),
        product = product.productEntityToDomain(),
        quantity = cartItem.quantity
    )
}