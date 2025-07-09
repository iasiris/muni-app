package com.iasiris.muniapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Product

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

fun CartItem.cartItemToEntity(): CartItemEntity {
    return CartItemEntity(
        id = id,
        productId = product.id,
        quantity = quantity
    )
}

fun CartItemEntity.cartItemEntityToDomain(): CartItem {
    return CartItem(
        id = id,
        product = Product(id = productId,"","","",0.0,false, ""),
        quantity = quantity
    )
}
