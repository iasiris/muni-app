package com.iasiris.muniapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val hasDrink: Boolean,
    val category: String
)

