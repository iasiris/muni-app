package com.iasiris.muniapp.domain.usecase.cartitem

import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.repository.CartItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCartItemUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(cartItem: CartItem) =
        withContext(Dispatchers.IO) { cartItemRepository.updateCartItem(cartItem) }
}