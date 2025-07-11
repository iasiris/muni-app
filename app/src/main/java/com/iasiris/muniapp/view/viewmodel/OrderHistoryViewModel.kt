package com.iasiris.muniapp.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iasiris.muniapp.data.local.datasource.OrderDataSource
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.OrderHistory
import com.iasiris.muniapp.domain.usecase.orderhistory.AddOrderUserCase
import com.iasiris.muniapp.domain.usecase.orderhistory.GetOrdersByUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrdersByUserIdUseCase: GetOrdersByUserIdUseCase,
    private val addOrderUseCase: AddOrderUserCase
) : ViewModel() {

    private val _orderHistoryUiState = MutableStateFlow(OrderHistoryUiState())
    val orderHistoryUiState: StateFlow<OrderHistoryUiState> = _orderHistoryUiState

    fun init() {
        getOrderHistory()
    }
    //TODO actualizar listado cuando se guarde un nuevo pedido

    fun getOrderHistory() {
        /*viewModelScope.launch {
            val orders = withContext(Dispatchers.IO) {
                orderDataSource.getOrdersByUserId("1")
            }
            _orderHistoryUiState.update { state ->
                state.copy(orderHistories = orders)
            }
        }*/
    }

    fun addOrderHistory(cartItems: List<CartItem>): Boolean {
        /*var isAdded = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                orderDataSource.addOrder(order)
            }
            getOrderHistory()
        }

        return isAdded*/
    }
}

data class OrderHistoryUiState(
    val orderHistory: List<OrderHistory> = emptyList()
)