package com.iasiris.muniapp.domain.model

data class Order(
    val id: String,
    val userId: String,
    val products: List<OrderItem>,
    val totalAmount: Double,
    val orderDate: String
)