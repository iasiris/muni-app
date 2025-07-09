package com.iasiris.muniapp.domain.model

data class Order(//TODO check val productsId: List<CartItem>
    val orderId: String,
    val productsId: List<CartItem>,
    val totalPrice: Int,
    val orderDate: String
)