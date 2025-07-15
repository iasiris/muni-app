package com.iasiris.muniapp.domain.mapper

import com.iasiris.muniapp.data.local.entity.OrderItemEntity
import com.iasiris.muniapp.data.local.entity.OrderItemWithProductEntity
import com.iasiris.muniapp.data.remote.dto.OrderItemDto
import com.iasiris.muniapp.domain.model.OrderItem


fun OrderItemWithProductEntity.orderItemWithProductEntityToDomain() = OrderItem(
    product = product.productEntityToDomain(),
    quantity = orderItem.quantity
)

fun OrderItem.orderItemToOrderItemDto() = OrderItemDto(
    product = product.productToProductDto(),
    quantity = quantity
)

fun OrderItemDto.orderItemDtoToEntity(orderId: String) = OrderItemEntity(
    orderId= orderId,
    productId = product.id.toString(),
    quantity = quantity
)

fun OrderItemDto.orderItemDtoToDomain() = OrderItem(
    product = product.productDtoToDomain(),
    quantity = quantity
)
