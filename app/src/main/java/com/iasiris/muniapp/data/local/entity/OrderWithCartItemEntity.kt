package com.iasiris.muniapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.iasiris.muniapp.domain.model.Order

data class OrderWithCartItemEntity(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val cartItems: List<CartItemWithProductEntity>
)

fun OrderWithCartItemEntity.orderWithCartItemEntityToDomain(): Order {
    return Order(
        id = order.id,
        userId = order.userId,
        cartItems = cartItems.map { it.cartItemWithProductEntityToDomain() },
        totalAmount = order.totalAmount,
        orderDate = order.orderDate
    )
}
