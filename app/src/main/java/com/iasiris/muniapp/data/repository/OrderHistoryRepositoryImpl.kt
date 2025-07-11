package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.OrderHistoryLocalDataSource
import com.iasiris.muniapp.data.remote.datasource.OrderHistoryRemoteDataSource
import com.iasiris.muniapp.domain.model.OrderHistory
import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class OrderHistoryRepositoryImpl @Inject constructor(
    private val orderHistoryLocalDataSource: OrderHistoryLocalDataSource,
    private val orderHistoryRemoteDataSource: OrderHistoryRemoteDataSource
): OrderHistoryRepository {
    override fun getOrdersByUserId(userId: String): List<OrderHistory> {
        TODO("Not yet implemented")
    }

    override suspend fun insertOrder(orderHistory: OrderHistory) {
        TODO("Not yet implemented")
    }

}