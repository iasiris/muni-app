package com.iasiris.muniapp.domain.usecase.product

import com.iasiris.muniapp.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String) =
        withContext(Dispatchers.IO) { productRepository.getProductById(productId) }
}