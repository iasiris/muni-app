package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.ProductLocalDataSource
import com.iasiris.muniapp.data.local.entity.productEntityToDomain
import com.iasiris.muniapp.data.remote.datasource.ProductRemoteDataSource
import com.iasiris.muniapp.data.remote.dto.productDtoToDomain
import com.iasiris.muniapp.data.remote.dto.productDtoToEntity
import com.iasiris.muniapp.domain.model.Product
import com.iasiris.muniapp.domain.repository.ProductRepository
import jakarta.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remote: ProductRemoteDataSource,
    private val local: ProductLocalDataSource
) : ProductRepository {
    override suspend fun getProducts(refreshData: Boolean): List<Product> {
        //TODO cargar info de la base de datos local, cargar de remoto si hay cambios en la base de datos
        return if (refreshData) {
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

    override suspend fun insertProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(productId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshProducts() {
        TODO("Not yet implemented")
    }
}


