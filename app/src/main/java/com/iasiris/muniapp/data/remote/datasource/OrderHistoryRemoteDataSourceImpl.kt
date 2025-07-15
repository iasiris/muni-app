package com.iasiris.muniapp.data.remote.datasource

import android.util.Log
import com.iasiris.muniapp.data.remote.OrderHistoryApiService
import com.iasiris.muniapp.data.remote.dto.OrderDto
import jakarta.inject.Inject

class OrderHistoryRemoteDataSourceImpl @Inject constructor(
    private val orderHistoryApiService: OrderHistoryApiService
) : OrderHistoryRemoteDataSource {
    override suspend fun getOrderHistoryByUserId(
        userId: String
    ): List<OrderDto> = orderHistoryApiService.getOrderHistory(userId)

    override suspend fun insertOrder(orderDto: OrderDto) {
        Log.d("OrderHistoryRemoteDataSourceImpl", "insertOrder: $orderDto")
        val test = orderHistoryApiService.postOrder(orderDto)
        Log.d("OrderHistoryRemoteDataSourceImpl", "insertOrder: $test")
        return test
    }
}