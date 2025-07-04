package com.iasiris.muniapp.data.local

import org.junit.jupiter.api.Test

class ProductDataSourceImplTest {
    private val productDataSource = ProductDataSourceImpl()

    @Test
    fun testGetProducts() {
        val products = productDataSource.getProducts()

        assert(products.isNotEmpty()) { "Product list should not be empty" }
        assert(products.size == 5) { "Expected 5 products, but got ${products.size}" }
    }

    @Test
    fun testGetProductById() {
        val product1 = productDataSource.getProductById("1")
        assert(product1 != null) { "Product with ID '1' should not be null" }
        assert(product1?.id == "1") { "Expected product ID '1', but got ${product1?.id}" }

        val product6 = productDataSource.getProductById("6")
        assert(product6 == null) { "Product with ID '6' should be null" }
    }


}