package com.iasiris.muniapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.domain.model.Order

data class OrderDto(
    @SerializedName("_id")
    val id: String,
    val userId: String,
    val cartItems: List<CartItemDto>,
    val totalAmount: Double,
    val orderDate: String
)

fun OrderDto.orderDtoToDomain(): Order {
    return Order(
        id = id,
        userId = userId,
        cartItems = cartItems.map { it.cartItemDtoToDomain() },
        totalAmount = totalAmount,
        orderDate = orderDate
    )
}

fun OrderDto.orderDtoToEntity(): OrderEntity {
    return OrderEntity(
        id = id.toInt(),
        totalItems = cartItems.size,
        totalAmount = totalAmount,
        orderDate = orderDate
    )
}