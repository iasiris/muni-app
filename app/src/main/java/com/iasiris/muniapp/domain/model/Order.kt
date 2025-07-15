package com.iasiris.muniapp.domain.model

data class Order(
    val id: String, //TODO porque se guarda en MONGO CON id=0
    val userId: String,
    val products: List<OrderItem>,
    val totalAmount: Double,
    val orderDate: String
)