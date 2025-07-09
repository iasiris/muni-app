package com.iasiris.muniapp.data.remote.datasource

import com.iasiris.muniapp.data.remote.ProductApiService
import com.iasiris.muniapp.data.remote.dto.ProductDto
import javax.inject.Inject

class ProductRemoteDataSourceImpl @Inject constructor(
    private val productApiService: ProductApiService
) : ProductRemoteDataSource {
    override suspend fun getProducts(): List<ProductDto> = productApiService.getProducts()

    override suspend fun getProductById(productId: String): ProductDto? {
        return try {
            productApiService.getProductById(productId)
        } catch (e: Exception) {
            null // TODO handle error
        }
    }
}