package com.iasiris.muniapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.iasiris.muniapp.data.local.entity.ProductEntity
import com.iasiris.muniapp.domain.model.Product

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

fun ProductDto.productDtoToDomain() = Product(id, name, description, imageUrl, price, hasDrink, category)
fun ProductDto.productDtoToEntity() = ProductEntity(id, name, description, imageUrl, price, hasDrink, category)