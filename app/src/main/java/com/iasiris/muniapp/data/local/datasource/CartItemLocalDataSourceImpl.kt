package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.data.local.dao.CartItemDao
import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.data.local.entity.CartItemWithProductEntity
import jakarta.inject.Inject

class CartItemLocalDataSourceImpl @Inject constructor(
    private val cartItemDao: CartItemDao
) : CartItemLocalDataSource {
    // CartItemEntity -> para operaciones CRUD simples sobre el carrito.
    // CartItemWithProductEntity ->para los datos completos del producto relacionado a cada ítem del carrito.

    override fun getCartItems(): List<CartItemEntity> {
        return cartItemDao.getCartItems()
    }

    override fun getCartItemsWithProducts(): List<CartItemWithProductEntity> {
        return cartItemDao.getCartItemsWithProducts()
    }

    override suspend fun getCartItemByProductId(productId: String): CartItemWithProductEntity? {
        return cartItemDao.getCartItemByProductId(productId)
    }

    override suspend fun insertCartItem(cartItem: CartItemEntity) {
        cartItemDao.insertCartItem(cartItem)
    }

    override suspend fun deleteCartItem(cartItemId: Int) {
        cartItemDao.deleteCartItem(cartItemId)
    }

    override suspend fun deleteCartItems() {
        cartItemDao.deleteCartItems()
    }
}