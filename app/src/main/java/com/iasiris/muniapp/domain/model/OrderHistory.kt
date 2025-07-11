package com.iasiris.muniapp.domain.model

data class OrderHistory(//TODO modify this class
    val orderId: String,
    val productsId: List<CartItem>,
    val totalPrice: Int,
    val orderDate: String
)