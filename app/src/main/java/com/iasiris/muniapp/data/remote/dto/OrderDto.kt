package com.iasiris.muniapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderDto(
    //@SerializedName("orderId")
    val id: String,
    val userId: String,
    val products: List<OrderItemDto>,
    val totalAmount: Double,
    val orderDate: String
)

