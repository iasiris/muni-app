package com.iasiris.muniapp.domain.usecase.cartitem

import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.repository.CartItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddCartItemUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(productId: String): CartItem = withContext(Dispatchers.IO) {
        cartItemRepository.insertCartItem(productId)
    }
}