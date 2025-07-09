package com.iasiris.muniapp.domain.usecase

import com.iasiris.muniapp.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(refreshData: Boolean) = productRepository.getProducts(refreshData)
}