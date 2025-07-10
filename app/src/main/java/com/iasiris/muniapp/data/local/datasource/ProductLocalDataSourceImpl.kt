package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.dao.ProductDao
import com.iasiris.muniapp.data.local.entity.ProductEntity
import jakarta.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductLocalDataSource {
    override suspend fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    override suspend fun insertProducts(products: List<ProductEntity>) {
        productDao.insertProducts(products)
    }

    override suspend fun updateProduct(product: ProductEntity) {
        productDao.updateProduct(product)
    }

    override suspend fun clearProducts() {
        productDao.deleteProducts()
    }

    override fun getProducts(): List<ProductEntity> {
        return productDao.getProducts()
    }

    override fun getProductById(productId: String): ProductEntity? {
        return productDao.getProductById(productId)
    }
}