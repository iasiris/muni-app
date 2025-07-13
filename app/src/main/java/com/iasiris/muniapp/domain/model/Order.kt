package com.iasiris.muniapp.domain.model

data class Order(
    val id: String,
    val userId: String,
    val cartItems: List<CartItem>,
    val totalPrice: Int,
    val orderDate: String
)