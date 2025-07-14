package com.iasiris.muniapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.domain.model.CartItem

data class CartItemDto(
    @SerializedName("_id")
    val id: String,
    val product: ProductDto,
    val quantity: Int = 1
)

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