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
                ?: throw IllegalArgumentException("La lista no pudo cargar productos")
            local.clearProducts()
            local.insertProducts(remoteProducts.map { it.productDtoToEntity() })
            remoteProducts.map { it.productDtoToDomain() }
        } else {
            val localProducts = local.getProducts()

            if (localProducts.isNullOrEmpty()) {
                val remoteProducts = remote.getProducts()
                    ?: throw IllegalArgumentException("La lista no pudo cargar productos")
                local.insertProducts(remoteProducts.map { it.productDtoToEntity() })
                remoteProducts.map { it.productDtoToDomain() }
            }

            localProducts.map { it.productEntityToDomain() }
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


