package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.ProductLocalDataSource
import com.iasiris.muniapp.data.remote.datasource.ProductRemoteDataSource
import com.iasiris.muniapp.domain.mapper.productDtoToDomain
import com.iasiris.muniapp.domain.mapper.productDtoToEntity
import com.iasiris.muniapp.domain.mapper.productEntityToDomain
import com.iasiris.muniapp.domain.model.Product
import com.iasiris.muniapp.domain.repository.ProductRepository
import jakarta.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remote: ProductRemoteDataSource,
    private val local: ProductLocalDataSource
) : ProductRepository {
    override suspend fun getProducts(refreshData: Boolean): List<Product> {

        return if (refreshData) { //TODO refresh con WorkManager
            val remoteProducts = remote.getProducts()
            local.clearProducts()
            local.insertProducts(remoteProducts.map { it.productDtoToEntity() })
            remoteProducts.map { it.productDtoToDomain() }
        } else {
            val localProducts = local.getProducts()
            if (localProducts.isNotEmpty()) {
                localProducts.map { it.productEntityToDomain() }
            } else {
                val remoteProducts = remote.getProducts()
                local.insertProducts(remoteProducts.map { it.productDtoToEntity() })
                remoteProducts.map { it.productDtoToDomain() }
            }
        }
    }

    override suspend fun getProductById(productId: String): Product? {
        val localProduct = local.getProductById(productId)
        return if (localProduct != null) {
            localProduct.productEntityToDomain()
        } else {
            val remoteProduct = remote.getProductById(productId)
            local.insertProduct(
                remoteProduct?.productDtoToEntity()
                    ?: throw IllegalArgumentException("Product not found")
            )
            remoteProduct.productDtoToDomain()
        }
    }
}


