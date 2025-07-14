package com.iasiris.muniapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iasiris.muniapp.data.remote.dto.ProductDto
import com.iasiris.muniapp.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val hasDrink: Boolean,
    val category: String
)

fun ProductEntity.productEntityToDomain() =
    Product(id, name, description, imageUrl, price, hasDrink, category)

fun ProductEntity.productEntityToDto() =
    ProductDto(id, name, description, imageUrl, price, hasDrink, category)
