package com.iasiris.muniapp.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iasiris.muniapp.data.local.OrderDataSource
import com.iasiris.muniapp.data.model.Order
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
    private val orderDataSource: OrderDataSource
) : ViewModel() {

    private val _orderHistoryUiState = MutableStateFlow(OrderHistoryUiState())
    val orderHistoryUiState: StateFlow<OrderHistoryUiState> = _orderHistoryUiState

    fun init() {
        getOrderHistory()
    }
    //TODO actualizar listado cuando se guarde un nuevo pedido

    fun getOrderHistory() {
        viewModelScope.launch {
            val orders = withContext(Dispatchers.IO) {
                orderDataSource.getOrdersByUserId("1")
            }
            _orderHistoryUiState.update { state ->
                state.copy(orders = orders)
            }
        }
    }
}

data class OrderHistoryUiState(
    val orders: List<Order> = emptyList()
)