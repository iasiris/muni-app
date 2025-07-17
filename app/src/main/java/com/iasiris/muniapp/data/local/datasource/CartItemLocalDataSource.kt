package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.data.local.entity.CartItemWithProductEntity

interface CartItemLocalDataSource {
    fun getCartItems(): List<CartItemEntity>
    fun getCartItemsWithProducts(): List<CartItemWithProductEntity>?
    fun getCartItemByProductId(productId: String): CartItemWithProductEntity?
    suspend fun insertCartItem(cartItem: CartItemEntity): CartItemWithProductEntity
    suspend fun updateCartItem(cartItem: CartItemEntity)
    suspend fun deleteCartItem(cartItemId: String)
    suspend fun deleteCartItems()
}