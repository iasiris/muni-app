package com.iasiris.muniapp.domain.usecase.orderhistory

import com.iasiris.muniapp.domain.model.OrderHistory
import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class AddOrderUserCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    suspend operator fun invoke(orderHistory: OrderHistory) =
        orderHistoryRepository.insertOrder(orderHistory)
}