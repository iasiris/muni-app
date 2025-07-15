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
            val remoteOrderHistory = remote.getOrderHistoryByUserId(userId) //get de Api

            local.clearOrderHistory()
            remoteOrderHistory.map { //insert all the orders with their respective items //guarda en ROOM
                val orderEntity = it.orderDtoToEntity()
                val orderItemEntityList =
                    it.products.map { orderItemDto ->
                        orderItemDto.orderItemDtoToEntity(orderEntity.id.toString())
                    }
                local.insertOrder(orderEntity, orderItemEntityList)
            }

            remoteOrderHistory.map { //genera List<Order>
                it.orderDtoToDomain()
            }
        } else {            //trae order de ROOM
            val localOrderHistory = local.getOrderHistory()
            if (localOrderHistory.isNotEmpty()) {
                localOrderHistory.map { //Mapeo de List<OrderEntity> a List<Order>
                    val orderItemsWithProductEntityList =
                        local.getOrderItemsWithProductsByOrderId(it.id.toInt())
                    val products =//TODO chequear problema con mappeo de products
                        //[{"_id":"68759ceb64ddc514cb1bca28","id":0,"userId":"123","products":[{"product":{"id":"string","name":"string","description":"string","imageUrl":"string","price":0,"hasDrink":true,"category":"string","_id":"68759ceb64ddc514cb1bca2a"},"quantity":0,"_id":"68759ceb64ddc514cb1bca29"}],"totalAmount":0,"orderDate":"string","createdAt":"2025-07-15T00:12:27.544Z","updatedAt":"2025-07-15T00:12:27.544Z","__v":0}]
                        orderItemsWithProductEntityList.map { orderItemWithProductEntity ->
                            orderItemWithProductEntity.orderItemWithProductEntityToDomain()
                        }
                    it.orderEntityToDomain(products)
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
                    local.insertOrder(orderEntity, orderItemEntityList)
                }

                remoteOrderHistory.map { //genera List<Order>
                    it.orderDtoToDomain()
                }
            }
        }
    }


    override suspend fun insertOrder(cartItems: List<CartItem>): Order {
        //TODO validar que la guarden se guardo en local y remote, si no se guardo en romote borrar en local
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

        remote.insertOrder(order.orderToOrderDto()) //TODO investigar porque rompe aca

        return order
    }

}