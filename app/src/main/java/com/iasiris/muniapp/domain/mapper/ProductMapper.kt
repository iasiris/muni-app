package com.iasiris.muniapp.domain.mapper

import com.iasiris.muniapp.data.local.entity.ProductEntity
import com.iasiris.muniapp.data.remote.dto.ProductDto
import com.iasiris.muniapp.domain.model.Product

fun ProductEntity.productEntityToDomain() =
    Product(id, name, description, imageUrl, price, hasDrink, category)

fun ProductEntity.productEntityToDto() =
    ProductDto(id, name, description, imageUrl, price, hasDrink, category)

fun ProductDto.productDtoToDomain() = Product(id, name, description, imageUrl, price, hasDrink, category)

fun ProductDto.productDtoToEntity() = ProductEntity(id, name, description, imageUrl, price, hasDrink, category)

fun Product.productToProductDto() = ProductDto(id, name, description, imageUrl, price, hasDrink, category)