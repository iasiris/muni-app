package com.iasiris.muniapp.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.usecase.orderhistory.AddOrderUseCase
import com.iasiris.muniapp.domain.usecase.orderhistory.GetOrdersByUserIdUseCase
import com.iasiris.muniapp.view.ui.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrdersByUserIdUseCase: GetOrdersByUserIdUseCase,
    private val addOrderUseCase: AddOrderUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _orderHistoryUiState = MutableStateFlow(OrderHistoryUiState())
    val orderHistoryUiState: StateFlow<OrderHistoryUiState> = _orderHistoryUiState

    fun loadOrderHistory(refreshData: Boolean = false) {
        viewModelScope.launch {
            _orderHistoryUiState.update { it.copy(screenState = ScreenState.Loading) }
            try {
                val orders = withContext(Dispatchers.IO) {
                    getOrdersByUserIdUseCase.invoke(
                        userPreferences.userIdFlow.first()!!,
                        refreshData
                    )
                }
                _orderHistoryUiState.update { state ->
                    state.copy(
                        orderHistory = orders,
                        screenState = ScreenState.Success(orders)
                    )
                }
            } catch (e: IOException) {
                _orderHistoryUiState.update { it.copy(screenState = ScreenState.Error("Sin conexión a internet")) }
                Log.e("com.iasiris.muniapp", "Error de red: ${e.message}")
            } catch (e: HttpException) {
                _orderHistoryUiState.update { it.copy(screenState = ScreenState.Error("Error de servidor")) }
                Log.e("com.iasiris.muniapp", "Error HTTP: ${e.message}")
            } catch (e: Exception) {
                _orderHistoryUiState.update { it.copy(screenState = ScreenState.Error("Ocurrió un error inesperado")) }
                Log.e("com.iasiris.muniapp", "Error inesperado: ${e.message}")
            }
        }
    }

    fun addOrder(cartItems: List<CartItem>) {
        viewModelScope.launch {
            //_orderHistoryUiState.update { it.copy(screenState = ScreenState.Loading) }
            val order = withContext(Dispatchers.IO) {
                addOrderUseCase.invoke(
                    userPreferences.userIdFlow.first()!!,
                    cartItems)
            }
            if (order != null) {
                _orderHistoryUiState.update { state ->
                    state.copy(
                        orderHistory = state.orderHistory + order,
                        isOrderAdded = true
                    )
                }
                loadOrderHistory()
            }
        }
    }

    fun resetOrderAdded() {
        _orderHistoryUiState.update { it.copy(isOrderAdded = false) }
    }
}

data class OrderHistoryUiState(
    val screenState: ScreenState<List<Order>> = ScreenState.Loading,
    val orderHistory: List<Order> = emptyList(),
    val isOrderAdded: Boolean = false
)