package com.iasiris.muniapp.data.remote

import com.iasiris.muniapp.data.remote.dto.ProductDto
import retrofit2.http.GET

interface ProductApiService {
    @GET("foods")
    suspend fun getProducts(): List<ProductDto>

    @GET("foods/{productId}")
    suspend fun getProductById(productId: String): ProductDto?
}