package com.iasiris.muniapp.data.remote.datasource

import com.iasiris.muniapp.data.remote.dto.OrderDto

interface OrderHistoryRemoteDataSource {
    suspend fun getOrderHistoryByUserId(userId: String): List<OrderDto>?
    suspend fun insertOrder(orderDto: OrderDto): String?
}