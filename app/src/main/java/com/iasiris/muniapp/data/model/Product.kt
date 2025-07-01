package com.iasiris.muniapp.data.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val hasDrink: Boolean,
    val category: String,
    val quantity: Int = 1
)