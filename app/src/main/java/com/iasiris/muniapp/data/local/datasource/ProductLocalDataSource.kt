package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.entity.ProductEntity

interface ProductLocalDataSource {
    suspend fun insertProduct(product: ProductEntity)
    suspend fun insertProducts(products: List<ProductEntity>)
    suspend fun updateProduct(product: ProductEntity)
    suspend fun clearProducts()
    fun getProducts(): List<ProductEntity>
    fun getProductById(productId: String): ProductEntity?
}