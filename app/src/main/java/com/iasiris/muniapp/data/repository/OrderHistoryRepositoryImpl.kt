package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.OrderHistoryLocalDataSource
import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.local.entity.OrderItemEntity
import com.iasiris.muniapp.data.remote.datasource.OrderHistoryRemoteDataSource
import com.iasiris.muniapp.domain.mapper.orderDtoToDomain
import com.iasiris.muniapp.domain.mapper.orderDtoToEntity
import com.iasiris.muniapp.domain.mapper.orderEntityToDomain
import com.iasiris.muniapp.domain.mapper.orderItemDtoToEntity
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

    override suspend fun getOrderHistoryByUserId(
        userId: String,
        refreshData: Boolean
    ): List<Order> {
        return if (refreshData) {
            getAndSaveInRemoteAndLocal(userId)
        } else {
            val localOrderHistory = local.getOrderHistory()

            if (localOrderHistory.isNullOrEmpty()) {
                return getAndSaveInRemoteAndLocal(userId)
            }

            localOrderHistory.map {
                val orderItemsWithProductEntityList =
                    local.getOrderItemsWithProductsByOrderId(it.id)
                val products =
                    orderItemsWithProductEntityList.map { orderItemWithProductEntity ->
                        orderItemWithProductEntity.orderItemWithProductEntityToDomain()
                    }
                it.orderEntityToDomain(userId, products)
            }
        }
    }

    private suspend fun getAndSaveInRemoteAndLocal(userId: String): List<Order> {
        val remoteOrderHistory = remote.getOrderHistoryByUserId(userId)
            ?: throw NoSuchElementException("No se encontraron ordenes de compra")

        local.deleteOrderHistory()

        remoteOrderHistory.forEach {
            val orderEntity = it.orderDtoToEntity()
            val orderItemEntityList = it.products.map { orderItemDto ->
                orderItemDto.orderItemDtoToEntity(orderEntity.id.toString())
            }
            val orderEntityWithId = local.insertOrder(orderEntity)
            local.insertOrderItems(orderEntityWithId.id, orderItemEntityList)
        }
        return remoteOrderHistory.map { it.orderDtoToDomain() }
    }

    override suspend fun insertOrder(
        userId: String,
        cartItems: List<CartItem>
    ): Order {//saves remote and local order
        val subTotal = cartItems.sumOf { it.product.price * it.quantity }
        val totalAmount = subTotal + (subTotal * 0.03)
        val orderEntity = OrderEntity(totalAmount = totalAmount, orderDate = returnDate())
        val orderItemsEntity = cartItems.map {
            OrderItemEntity(
                orderId = orderEntity.id.toString(),
                productId = it.product.id,
                quantity = it.quantity
            )
        }

        val orderEntityWithId = local.insertOrder(orderEntity)
        val orderItemsWithProductEntity =
            local.insertOrderItems(orderEntityWithId.id, orderItemsEntity)
        val order = orderEntityWithId.orderEntityToDomain(
            userId,
            orderItemsWithProductEntity.map { it.orderItemWithProductEntityToDomain() }
        )

        val remoteOrder = remote.insertOrder(order.orderToOrderDto())
        if (remoteOrder.isNullOrEmpty()) {
            local.deleteOrderByOrderId(orderEntity.id)
            throw NoSuchElementException("No se pudo insertar la orden")
        }
        return order
    }

    override suspend fun clearOrderHistory() {
        local.deleteOrderHistory()
    }
}