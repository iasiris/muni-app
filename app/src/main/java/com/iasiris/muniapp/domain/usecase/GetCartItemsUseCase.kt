package com.iasiris.muniapp.domain.usecase

import com.iasiris.muniapp.data.repository.CartItemRepositoryImpl
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepositoryImpl
) {
    operator fun invoke() = cartItemRepository.getCartItemsWithProducts()
}