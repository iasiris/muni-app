package com.iasiris.muniapp.data.remote.dto

data class OrderDto(
    val id: String,
    val userId: String,
    val products: List<OrderItemDto>,
    val totalAmount: Double,
    val orderDate: String
)

