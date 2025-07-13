package com.iasiris.muniapp.data.remote.datasource

import com.iasiris.muniapp.data.remote.dto.OrderDto
import com.iasiris.muniapp.domain.model.Order

interface OrderHistoryRemoteDataSource {
    suspend fun getOrderHistoryByUserId(userId: String): List<OrderDto>
    suspend fun insertOrder(order: Order): Order //TODO CHECH THIS DATAFLOW
}