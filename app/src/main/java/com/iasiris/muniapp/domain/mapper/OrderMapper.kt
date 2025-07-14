package com.iasiris.muniapp.domain.mapper

import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.remote.dto.OrderDto
import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.model.OrderItem

fun OrderEntity.orderEntityToDomain(orderItems: List<OrderItem>) = Order(
    id = id.toString(),
    userId = "", // Assuming userId is not stored in OrderEntity
    products = orderItems,
    totalAmount = totalAmount,
    orderDate = orderDate
)

fun Order.orderToOrderDto() = OrderDto(
    id = id,
    userId = userId,
    products = products.map { it.orderItemToOrderItemDto() },
    totalAmount = totalAmount,
    orderDate = orderDate
)