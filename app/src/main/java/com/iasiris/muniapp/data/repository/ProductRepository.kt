package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.database.dao.ProductDao
import com.iasiris.muniapp.data.database.entity.ProductEntity
import com.iasiris.muniapp.data.model.Product
import jakarta.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val productDataSource: ProductDataSource
) {

    suspend fun insertProducts(products: List<ProductEntity>) {
        productDao.insertProducts(products)
    }

    suspend fun getAllProducts(): List<Product> {
        val productsFromApi = productDataSource.getAllProducts()
        val productEntities = productsFromApi.toProductEntityList()
        insertProducts(productEntities)
        return productsFromApi
    }
}

fun Product.toProductEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    price = price,
    hasDrink = hasDrink,
    category = category,
    quantity = quantity
)

fun List<Product>.toProductEntityList(): List<ProductEntity> = map { it.toProductEntity() }