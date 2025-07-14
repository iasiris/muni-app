package com.iasiris.muniapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Product

//TODO ver clase 30/06 para ver como armar order y orderItem

@Entity(
    tableName = "cart_items",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    var quantity: Int = 1
)

fun CartItem.cartItemToEntity() = CartItemEntity(id.toInt(), product.id, quantity)

fun CartItemEntity.cartItemEntityToDomain() = CartItem(
    id = id.toString(),
    product = Product(id = productId, "", "", "", 0.0, false, ""),
    quantity = quantity
)
