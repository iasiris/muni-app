package com.iasiris.muniapp.data.model

data class CartItem(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val hasDrink: Boolean,
    val quantity: Int
)

data class Order(
    val orderId: String,
    val productsId: List<CartItem>,
    val totalPrice: Int,
    val orderDate: String
)