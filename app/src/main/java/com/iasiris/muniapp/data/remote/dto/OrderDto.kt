package com.iasiris.muniapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order
import kotlin.String

data class OrderDto(
    @SerializedName("_id")
    val id: String,
    val userId: String,
    val cartItems: List<CartItem>, //todo CartItem contiene Product
    val totalPrice: Int,
    val orderDate: String
)

fun OrderDto.orderDtoToModel(): Order {
    return Order(
        id = id,
        userId = userId,
        cartItems = cartItems.map {/*todo dto to domain*/},
        totalPrice = totalPrice,
        orderDate = orderDate
    )
}