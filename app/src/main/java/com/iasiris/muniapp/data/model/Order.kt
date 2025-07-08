package com.iasiris.muniapp.data.model

data class CartItem(
    val id: Int = 0,
    val product: Product,
    val quantity: Int = 1
)

data class Order(//TODO check val productsId: List<CartItem>
    val orderId: String,
    val productsId: List<CartItem>,
    val totalPrice: Int,
    val orderDate: String
)