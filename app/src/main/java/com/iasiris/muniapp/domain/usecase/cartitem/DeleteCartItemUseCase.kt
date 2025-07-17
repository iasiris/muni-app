package com.iasiris.muniapp.domain.usecase.cartitem

import com.iasiris.muniapp.domain.repository.CartItemRepository
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(cartItemId: String) = cartItemRepository.deleteCartItem(cartItemId)
}