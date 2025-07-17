package com.iasiris.muniapp.data.remote.datasource

import com.iasiris.muniapp.data.remote.dto.ProductDto

interface ProductRemoteDataSource {
    suspend fun getProducts(): List<ProductDto>?
    suspend fun getProductById(productId: String): ProductDto?
}