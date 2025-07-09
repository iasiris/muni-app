package com.iasiris.muniapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.data.local.entity.CartItemWithProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)

    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE id = :cartItemId")
    suspend fun deleteCartItem(cartItemId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun deleteCartItems()

    @Query("SELECT * FROM cart_items")
    fun getCartItems(): List<CartItemEntity>

    @Transaction
    @Query("SELECT * FROM cart_items")
    fun getCartItemsWithProducts(): List<CartItemWithProductEntity>

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItemByProductId(productId: String): CartItemWithProductEntity? //todo check this return type
}