package com.iasiris.muniapp.domain.usecase.orderhistory

import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetOrdersByUserIdUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    suspend operator fun invoke(userId: String, refreshData: Boolean): List<Order> =
        withContext(Dispatchers.IO) {
            orderHistoryRepository.getOrderHistoryByUserId(userId, refreshData)
        }
}