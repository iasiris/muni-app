package com.iasiris.muniapp.domain.usecase.orderhistory

import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class AddOrderUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    suspend operator fun invoke(userId: String, cartItems: List<CartItem>): Order =
        orderHistoryRepository.insertOrder(userId, cartItems)
}