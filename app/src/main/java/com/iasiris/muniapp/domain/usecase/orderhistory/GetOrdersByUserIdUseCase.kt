package com.iasiris.muniapp.domain.usecase.orderhistory

import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import jakarta.inject.Inject

class GetOrdersByUserIdUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    operator fun invoke(userId: String, refreshData: Boolean) =
        orderHistoryRepository.getOrderHistoryByUserId(userId, refreshData)
}