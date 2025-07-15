package com.iasiris.muniapp.data.remote.dto

data class OrderDto(
    val id: String, //TODO porque se guarda en MONGO CON id=0
    val userId: String,
    val products: List<OrderItemDto>,
    val totalAmount: Double,
    val orderDate: String
)

