package com.iasiris.muniapp.domain.mapper

import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.data.local.entity.CartItemWithProductEntity
import com.iasiris.muniapp.data.remote.dto.CartItemDto
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

fun CartItemDto.cartItemDtoToDomain() = CartItem(
    id = id,
    product = product.productDtoToDomain(),
    quantity = quantity
)

fun CartItemDto.cartItemDtoToEntity() = CartItemEntity(
    id = id.toInt(),
    productId = product.id,
    quantity = quantity
)