package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.model.Product

interface ProductDataSource {
    fun getAllProducts(): List<Product>
    fun getProductById(productId: String): Product?
}