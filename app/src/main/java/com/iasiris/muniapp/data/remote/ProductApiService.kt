package com.iasiris.muniapp.data.remote

import com.iasiris.muniapp.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("foods")
    suspend fun getProducts(): List<ProductDto>

    @GET("foods/{productId}")
    suspend fun getProductById(@Path("productId")productId: String): ProductDto?
}