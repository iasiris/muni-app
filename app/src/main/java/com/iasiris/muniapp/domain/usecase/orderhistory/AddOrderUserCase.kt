package com.iasiris.muniapp.domain.usecase.orderhistory

import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class AddOrderUserCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    suspend operator fun invoke(cartItems: List<CartItem>): Order =
        orderHistoryRepository.insertOrder(cartItems)
}