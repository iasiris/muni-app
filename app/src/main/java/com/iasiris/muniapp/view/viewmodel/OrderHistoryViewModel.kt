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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
                val userId = userPreferences.userIdFlow.first()
                    ?: throw NoSuchElementException("El usuario no esta loggeado")
                val orderHistory = getOrdersByUserIdUseCase.invoke(
                    userId,
                    refreshData
                )

                _orderHistoryUiState.update { state ->
                    state.copy(
                        orderHistory = orderHistory,
                        screenState = ScreenState.Success("")
                    )
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is IOException -> "Sin conexión a internet"
                    is HttpException -> "Error de servidor"
                    else -> "Ocurrió un error inesperado"
                }
                _orderHistoryUiState.update { it.copy(screenState = ScreenState.Error(errorMessage)) }
                Log.e("com.iasiris.muniapp", "Error: ${e.message}")
            }
        }
    }

    fun addOrder(cartItems: List<CartItem>) {
        viewModelScope.launch {
            _orderHistoryUiState.update { it.copy(screenState = ScreenState.Loading) }
            try {
                val userId = userPreferences.userIdFlow.first()
                    ?: throw NoSuchElementException("El usuario no esta loggeado")
                val order = addOrderUseCase.invoke(userId, cartItems)

                _orderHistoryUiState.update { state ->
                    state.copy(
                        orderHistory = state.orderHistory + order,
                        isOrderAdded = true,
                        screenState = ScreenState.Success("")
                    )
                }
                loadOrderHistory()

            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is NoSuchElementException -> e.message ?: "Error de carga"
                    is IOException -> "Sin conexión a internet"
                    is HttpException -> "Error de servidor"
                    else -> "Ocurrió un error inesperado"
                }
                _orderHistoryUiState.update { it.copy(screenState = ScreenState.Error(errorMessage)) }
                Log.e("com.iasiris.muniapp", "Error: ${e.message}")
            }
        }
    }

    fun resetOrderAdded() {
        _orderHistoryUiState.update { it.copy(isOrderAdded = false) }
    }
}

data class OrderHistoryUiState(
    val screenState: ScreenState<String> = ScreenState.Loading,
    val orderHistory: List<Order> = emptyList(),
    val isOrderAdded: Boolean = false
)