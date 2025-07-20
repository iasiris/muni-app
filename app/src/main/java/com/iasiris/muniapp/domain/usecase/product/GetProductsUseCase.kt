package com.iasiris.muniapp.domain.usecase.product

import com.iasiris.muniapp.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(refreshData: Boolean) = withContext(Dispatchers.IO) {
        productRepository.getProducts(refreshData)
    }
}