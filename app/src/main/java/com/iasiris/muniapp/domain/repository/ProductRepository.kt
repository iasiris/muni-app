package com.iasiris.muniapp.domain.repository

import com.iasiris.muniapp.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(refreshData: Boolean): List<Product>
    suspend fun getProductById(productId: String): Product?
}

