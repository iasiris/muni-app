package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.CartItemLocalDataSource
import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.domain.mapper.cartItemEntityToDomain
import com.iasiris.muniapp.domain.mapper.cartItemToEntity
import com.iasiris.muniapp.domain.mapper.cartItemWithProductEntityToDomain
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Product
import com.iasiris.muniapp.domain.repository.CartItemRepository
import jakarta.inject.Inject


class CartItemRepositoryImpl @Inject constructor(
    private val cartItemLocalDataSource: CartItemLocalDataSource
) : CartItemRepository {

    override fun getCartItems(): List<CartItem> {//TODO chequear real uso de esto
        val localCarItems = cartItemLocalDataSource.getCartItems()
        return localCarItems.map { it.cartItemEntityToDomain() }
    }

    override fun getCartItemsWithProducts(): List<CartItem> {
        val localCartItems = cartItemLocalDataSource.getCartItemsWithProducts()
        return localCartItems.map { it.cartItemWithProductEntityToDomain() }
    }

    override suspend fun getCartItemByProductId(productId: String): CartItem? { //siempre va a retonar un CartItemWithProductEntity
        val cartItemEntity = cartItemLocalDataSource.getCartItemByProductId(productId)
        return cartItemEntity?.cartItemWithProductEntityToDomain()
    }

    override suspend fun insertCartItem(product: Product): CartItem {
        val cartItemWithProductEntity = cartItemLocalDataSource.insertCartItem(CartItemEntity(productId = product.id))
        return cartItemWithProductEntity.cartItemWithProductEntityToDomain()
    }

    override suspend fun updateCartItem(cartItem: CartItem) {
        val cartItemEntity = cartItem.cartItemToEntity()
        cartItemLocalDataSource.updateCartItem(cartItemEntity)
    }

    override suspend fun deleteCartItem(cartItemId: Int) {
        cartItemLocalDataSource.deleteCartItem(cartItemId)
    }

    override suspend fun deleteCartItems() {
        cartItemLocalDataSource.deleteCartItems()
    }
}


