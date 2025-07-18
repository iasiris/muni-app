package com.iasiris.muniapp.domain.usecase.orderhistory

import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class DeleteOrderHistoryUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    suspend operator fun invoke() =
        orderHistoryRepository.clearOrderHistory()

}