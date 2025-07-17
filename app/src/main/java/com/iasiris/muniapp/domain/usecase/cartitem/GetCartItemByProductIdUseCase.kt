package com.iasiris.muniapp.domain.usecase.cartitem

import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.repository.CartItemRepository
import javax.inject.Inject

class GetCartItemByProductIdUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    operator fun invoke(productId: String): CartItem? =
        cartItemRepository.getCartItemByProductId(productId)
}