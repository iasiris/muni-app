package com.iasiris.muniapp.domain.repository

import com.iasiris.muniapp.domain.model.CartItem

interface CartItemRepository {
    fun getCartItems(): List<CartItem>
    fun getCartItemsWithProducts(): List<CartItem>?
    fun getCartItemByProductId(productId: String): CartItem?
    suspend fun updateCartItem(cartItem: CartItem)
    suspend fun insertCartItem(productId: String): CartItem
    suspend fun deleteCartItem(cartItemId: String)
    suspend fun deleteCartItems()
}