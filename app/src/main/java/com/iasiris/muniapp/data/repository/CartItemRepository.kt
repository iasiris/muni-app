package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.database.dao.CartItemDao
import com.iasiris.muniapp.data.database.entity.CartItemEntity
import com.iasiris.muniapp.data.database.entity.CartItemWithProductEntity
import com.iasiris.muniapp.data.database.entity.ProductEntity
import com.iasiris.muniapp.data.model.CartItem
import com.iasiris.muniapp.data.model.Product
import com.iasiris.muniapp.data.repository.toCartItemList
import javax.inject.Inject

class CartItemRepository @Inject constructor( //Guarda localmente los productos del carrito
    private val cartItemDao: CartItemDao
) {
    // CartItemEntity -> para operaciones CRUD simples sobre el carrito.
    // CartItemWithProductEntity ->para los datos completos del producto relacionado a cada ítem del carrito.
    fun getAllCartItems(): List<CartItem> {
        return cartItemDao.getAllCartItems()//todo hacer que retorne una lista de cartItems
    }

    fun getAllCartItemsWithProducts(): List<CartItemWithProductEntity> {
        return cartItemDao.getCartItemsWithProducts()
    }

    suspend fun getCartItemByProductId(productId: String): CartItemEntity? {
        return cartItemDao.getCartItemByProductId(productId)
    }

    suspend fun insertCartItem(cartItem: CartItemEntity) {
        cartItemDao.insertCartItem(cartItem)
    }

    suspend fun deleteCartItem(cartItemId: Int) {
        cartItemDao.deleteCartItem(cartItemId)
    }

    suspend fun deleteCartItems() {
        cartItemDao.deleteCartItems()
    }

}

fun CartItemWithProductEntity.toCartItem(): CartItem {
    return CartItem(
        id = cartItem.id,
        product = product.toProduct(),
        quantity = cartItem.quantity
    )
}

fun CartItem.toCartItemEntity(): CartItemEntity {
    return CartItemEntity(
        id = id,
        productId = product.id,
        quantity = quantity
    )
}

private fun ProductEntity.toProduct(): Product = Product(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    price = price,
    hasDrink = hasDrink,
    category = category,
    quantity = quantity
)

fun List<CartItemWithProductEntity>.toCartItemList(): List<CartItem> {
    return map { it.toCartItem() }
}
