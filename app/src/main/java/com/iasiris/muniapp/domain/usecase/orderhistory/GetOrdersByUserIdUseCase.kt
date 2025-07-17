package com.iasiris.muniapp.domain.usecase.orderhistory

import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import jakarta.inject.Inject

class GetOrdersByUserIdUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    suspend operator fun invoke(userId: String, refreshData: Boolean): List<Order> =
        orderHistoryRepository.getOrderHistoryByUserId(userId, refreshData)
}