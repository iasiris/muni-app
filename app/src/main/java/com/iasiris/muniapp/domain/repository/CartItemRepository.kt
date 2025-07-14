package com.iasiris.muniapp.domain.repository

import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Product

interface CartItemRepository {
    fun getCartItems(): List<CartItem>
    fun getCartItemsWithProducts(): List<CartItem>
    suspend fun getCartItemByProductId(productId: String): CartItem?
    suspend fun updateCartItem(cartItem: CartItem)
    suspend fun insertCartItem(product: Product): CartItem
    suspend fun deleteCartItem(cartItemId: Int)
    suspend fun deleteCartItems()
}