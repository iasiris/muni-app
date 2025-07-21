package com.iasiris.muniapp.domain.usecase.cartitem

import com.iasiris.muniapp.domain.repository.CartItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(cartItemId: String) =
        withContext(Dispatchers.IO) { cartItemRepository.deleteCartItem(cartItemId) }
}