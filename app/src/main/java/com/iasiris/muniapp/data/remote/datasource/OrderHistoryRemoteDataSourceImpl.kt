package com.iasiris.muniapp.data.remote.datasource

import com.iasiris.muniapp.data.remote.OrderHistoryApiService
import com.iasiris.muniapp.data.remote.dto.OrderDto
import com.iasiris.muniapp.domain.model.Order
import jakarta.inject.Inject

class OrderHistoryRemoteDataSourceImpl @Inject constructor(
    private val orderHistoryApiService: OrderHistoryApiService
) : OrderHistoryRemoteDataSource {
    override suspend fun getOrderHistoryByUserId(
        userId: String
    ): List<OrderDto> = orderHistoryApiService.getOrderHistory(userId)

    override suspend fun insertOrder(order: Order): Order { //TODO CHECH THIS DATAFLOW
        TODO("Not yet implemented")
    }
}