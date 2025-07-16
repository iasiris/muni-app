package com.iasiris.muniapp.data.repository

import android.util.Log
import com.iasiris.muniapp.data.local.datasource.OrderHistoryLocalDataSource
import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.local.entity.OrderItemEntity
import com.iasiris.muniapp.data.local.entity.OrderItemWithProductEntity
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

    override suspend fun getOrderHistoryByUserId( //TODO check logic in this function
        userId: String,
        refreshData: Boolean
    ): List<Order> {
        return if (refreshData) {
            val remoteOrderHistory = remote.getOrderHistoryByUserId(userId)

            local.clearOrderHistory()
            remoteOrderHistory.map { //insert all the orders with their respective items //guarda en ROOM
                val orderEntity = it.orderDtoToEntity()
                val orderItemEntityList =
                    it.products.map { orderItemDto ->
                        orderItemDto.orderItemDtoToEntity(orderEntity.id.toString())
                    }
                val orderEntityWithId = local.insertOrder(orderEntity)
                local.insertOrderItems(orderEntityWithId.id, orderItemEntityList)
            }

            remoteOrderHistory.map { //genera List<Order>
                it.orderDtoToDomain()
            }
        } else {            //trae order de ROOM
            val localOrderHistory = local.getOrderHistory()
            if (localOrderHistory.isNotEmpty()) {
                localOrderHistory.map { //Mapeo de List<OrderEntity> a List<Order>
                    val orderItemsWithProductEntityList =
                        local.getOrderItemsWithProductsByOrderId(it.id)
                    val products = orderItemsWithProductEntityList.map { orderItemWithProductEntity ->
                            orderItemWithProductEntity.orderItemWithProductEntityToDomain()
                        }
                    it.orderEntityToDomain(userId,products)
                }
            } else { //Pide a la API
                val remoteOrderHistory = remote.getOrderHistoryByUserId(userId)
                local.clearOrderHistory()
                remoteOrderHistory.map { //insert all the orders with their respective items //guarda en ROOM
                    val orderEntity = it.orderDtoToEntity()
                    val orderItemEntityList =
                        it.products.map { orderItemDto ->
                            orderItemDto.orderItemDtoToEntity(orderEntity.id.toString())
                        }
                    val orderEntityWithId = local.insertOrder(orderEntity)
                    local.insertOrderItems(orderEntityWithId.id, orderItemEntityList)
                }

                remoteOrderHistory.map { //genera List<Order>
                    it.orderDtoToDomain()
                }
            }
        }
    }

    override suspend fun insertOrder(userId: String, cartItems: List<CartItem>): Order {
        val subTotal = cartItems.sumOf { it.product.price * it.quantity }
        val deliveryFee = 0.03
        val orderEntity = OrderEntity(
            totalAmount = cartItems.sumOf { subTotal + (subTotal * deliveryFee) },
            orderDate = returnDate()
        )
        val orderItemsEntity = cartItems.map { cartItem ->
            OrderItemEntity(
                orderId = orderEntity.id.toString(),
                productId = cartItem.product.id,
                quantity = cartItem.quantity
            )
        }

        val orderEntityWithId = local.insertOrder(orderEntity)
        val orderItemsWithProductEntity = local.insertOrderItems(orderEntityWithId.id, orderItemsEntity)
        Log.d("OrderHistoryRepositoryImpl","orderItemsWithProductEntity: $orderItemsWithProductEntity")


        val order = orderEntityWithId.orderEntityToDomain(
            userId,
            orderItemsWithProductEntity.map { it.orderItemWithProductEntityToDomain() }
        )
        Log.d("OrderHistoryRepositoryImpl", "order: $order")
        // Insert order into remote service
        val orderDto = order.orderToOrderDto()
        Log.d("OrderHistoryRepositoryImpl", "order: $orderDto")
        val isSuccessful = remote.insertOrder(order.orderToOrderDto())
        if (isSuccessful) {
            return order
        } else {
            local.clearOrderByOrderId(orderEntity.id)
            throw Exception("Failed to insert order in remote service")
        }
    }

}