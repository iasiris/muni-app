package com.iasiris.muniapp.domain.model

import com.iasiris.muniapp.utils.CommonUtils.Companion.returnDate

data class Order(
    val id: String,
    val userId: String,
    val cartItems: List<CartItem>,
    val totalAmount: Double,
    val orderDate: String = returnDate()
)