package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.OrderHistoryLocalDataSource
import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.local.entity.OrderItemEntity
import com.iasiris.muniapp.data.remote.datasource.OrderHistoryRemoteDataSource
import com.iasiris.muniapp.domain.mapper.orderEntityToDomain
import com.iasiris.muniapp.domain.mapper.orderItemWithProductEntityToDomain
import com.iasiris.muniapp.domain.mapper.orderToOrderDto
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import com.iasiris.muniapp.utils.CommonUtils.Companion.returnDate
import javax.inject.Inject

class OrderHistoryRepositoryImpl @Inject constructor(
    private val remote: OrderHistoryRemoteDataSource,
    private val local: OrderHistoryLocalDataSource
) : OrderHistoryRepository {

    //TODO ver clase 30/06 para ver como armar order y orderItem
    override suspend fun getOrderHistoryByUserId(
        userId: String,
        refreshData: Boolean
    ): List<Order> {
        TODO("Not yet implemented")
        /*return if (refreshData) {
            val remoteOrderHistory = remote.getOrderHistoryByUserId(userId)
            local.clearOrderHistory()
            local.insertOrderHistory(remoteOrderHistory.map { it.orderDtoToEntity() })
            remoteOrderHistory.map { it.orderDtoToDomain() }
        } else {
            val localOrderHistory = local.getOrderHistory()
            if (localOrderHistory.isNotEmpty()) {
                localOrderHistory.map { it.orderWithCartItemEntityToDomain() }
            } else {
                val remoteOrderHistory = remote.getOrderHistoryByUserId(userId)
                local.insertOrderHistory(remoteOrderHistory.map { it.orderDtoToEntity() })
                remoteOrderHistory.map { it.orderDtoToDomain() }
            }
        }*/
    }

    override suspend fun insertOrder(cartItems: List<CartItem>): Order {
        val orderEntity = OrderEntity(
            totalAmount = cartItems.sumOf { it.product.price * it.quantity },
            orderDate = returnDate()
        )
        val orderItemsEntity = cartItems.map { cartItem ->
            OrderItemEntity(
                orderId = orderEntity.id.toString(),
                productId = cartItem.product.id,
                quantity = cartItem.quantity
            )
        }

        val orderItemsWithProductEntity = local.insertOrder(orderEntity, orderItemsEntity)

        val order = orderEntity.orderEntityToDomain(
            orderItemsWithProductEntity.map { it.orderItemWithProductEntityToDomain() }
        )

        remote.insertOrder(order.orderToOrderDto())
        return order
    }

}