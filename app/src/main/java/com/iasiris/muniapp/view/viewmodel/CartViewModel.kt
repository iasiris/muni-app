package com.iasiris.muniapp.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iasiris.muniapp.data.model.CartItem
import com.iasiris.muniapp.data.repository.CartItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartItemRepository: CartItemRepository
) : ViewModel() {
    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState

    fun init() {
        getCartItems()
    }

    fun onIncreaseCartItem(cartItem: CartItem) {
        _cartUiState.update { state ->
            val updatedCartItems = state.cartItems.map {
                if (it.product.id == cartItem.product.id && it.quantity < state.MAX_CART_ITEM_QUANTITY) {
                    it.copy(quantity = it.quantity + 1)
                } else it
            }
            state.copy(cartItems = updatedCartItems)
        }

        updateTotal()
    }

    fun onDecreaseCartItem(cartItem: CartItem) {
        _cartUiState.update { state ->
            val updatedCartItems = state.cartItems.mapNotNull {
                if (it.product.id == cartItem.product.id && it.quantity > state.MIN_CART_ITEM_QUANTITY) {
                    it.copy(quantity = it.quantity - 1)
                } else it
            }
            state.copy(cartItems = updatedCartItems)
        }
        updateTotal()
    }

    fun onRemoveCartItem(cartItem: CartItem) {
        _cartUiState.update { state ->
            val updatedCartItems = state.cartItems.filterNot {
                it.product.id == cartItem.product.id
            }
            state.copy(cartItems = updatedCartItems)
        }
        updateTotal()
    }

    private fun getCartItems() {
        viewModelScope.launch {
            val allCartItems = withContext(Dispatchers.IO) {
                cartItemRepository.getAllCartItems()
            }
            _cartUiState.update { state ->
                state.copy(cartItems = allCartItems)
            }
            updateTotal()
        }
    }

    private fun updateTotal() {//TODO guardar cartItems en DB
        _cartUiState.update { state ->
            val subTotal = state.cartItems.sumOf { it.product.price * it.quantity }
            val deliveryFee = Math.round(subTotal * state.FEE_PERCENTAGE * 100) / 100.0
            val totalAmount = subTotal + state.deliveryFee
            state.copy(
                subTotal = subTotal,
                deliveryFee = deliveryFee,
                totalAmount = totalAmount
            )
        }
    }
}

data class CartUiState(
    val MAX_CART_ITEM_QUANTITY: Int = 10,
    val MIN_CART_ITEM_QUANTITY: Int = 1,
    val FEE_PERCENTAGE: Double = 0.03,
    val cartItems: List<CartItem> = emptyList(),
    val subTotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val totalAmount: Double = 0.0
)