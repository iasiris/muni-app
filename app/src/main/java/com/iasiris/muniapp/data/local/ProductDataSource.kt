package com.iasiris.muniapp.data.local

import com.iasiris.muniapp.data.model.Product

interface ProductDataSource {

    fun getProducts(): List<Product>
    fun getProductById(productId: String): Product?
}