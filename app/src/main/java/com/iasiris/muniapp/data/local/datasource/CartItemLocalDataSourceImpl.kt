package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.dao.CartItemDao
import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.data.local.entity.CartItemWithProductEntity
import jakarta.inject.Inject

class CartItemLocalDataSourceImpl @Inject constructor(
    private val cartItemDao: CartItemDao
) : CartItemLocalDataSource {
    override fun getCartItems(): List<CartItemEntity> {
        return cartItemDao.getCartItems()
    }

    override fun getCartItemsWithProducts(): List<CartItemWithProductEntity>? {
        return cartItemDao.getCartItemsWithProducts()
    }

    override fun getCartItemByProductId(productId: String): CartItemWithProductEntity? {
        return cartItemDao.getCartItemByProductId(productId)
    }

    override suspend fun insertCartItem(cartItem: CartItemEntity): CartItemWithProductEntity {
        cartItemDao.insertCartItem(cartItem)
        return cartItemDao.getCartItemByProductId(cartItem.productId)
            ?: throw NoSuchElementException("CartItem no se pudo encontrar despues de insert")
    }

    override suspend fun updateCartItem(cartItem: CartItemEntity) {
        cartItemDao.updateCartItem(cartItem)
    }

    override suspend fun deleteCartItem(cartItemId: String) {
        cartItemDao.deleteCartItem(cartItemId)
    }

    override suspend fun deleteCartItems() {
        cartItemDao.deleteCartItems()
    }
}