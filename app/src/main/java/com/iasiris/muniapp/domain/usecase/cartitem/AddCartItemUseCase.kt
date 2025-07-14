package com.iasiris.muniapp.domain.usecase.cartitem

import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Product
import com.iasiris.muniapp.domain.repository.CartItemRepository
import javax.inject.Inject

class AddCartItemUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(product: Product): CartItem = cartItemRepository.insertCartItem(product)
}