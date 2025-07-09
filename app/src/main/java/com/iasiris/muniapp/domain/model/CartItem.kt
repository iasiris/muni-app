package com.iasiris.muniapp.domain.model

data class CartItem(
    val id: Int = 0,
    val product: Product,
    val quantity: Int = 1
)
