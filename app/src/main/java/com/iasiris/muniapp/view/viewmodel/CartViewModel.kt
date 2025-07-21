package com.iasiris.muniapp.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Product
import com.iasiris.muniapp.domain.usecase.cartitem.AddCartItemUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.DeleteCartItemUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.DeleteCartItemsUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.GetCartItemByProductIdUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.GetCartItemsUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.UpdateCartItemUseCase
import com.iasiris.muniapp.view.ui.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val getCartItemByProductIdUseCase: GetCartItemByProductIdUseCase,
    private val addCartItemUseCase: AddCartItemUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val deleteCartItem: DeleteCartItemUseCase,
    private val deleteCartItemsUseCase: DeleteCartItemsUseCase
) : ViewModel() {
    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState

    fun init() {
        getCartItems()
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            _cartUiState.update { it.copy(screenState = ScreenState.Loading) }
            try {
                val existingCartItem = getCartItemByProductIdUseCase.invoke(product.id)

                if (existingCartItem == null) {
                    val newCartItem = addCartItemUseCase.invoke(product.id)
                    _cartUiState.update { state ->
                        val updatedCartItems = state.cartItems + newCartItem
                        state.copy(
                            cartItems = updatedCartItems,
                            screenState = ScreenState.Success(""),
                        )
                    }
                    updateTotal()
                } else {
                    onIncreaseCartItem(existingCartItem)
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun onIncreaseCartItem(cartItem: CartItem) {
        viewModelScope.launch() {
            try {
                val cartItemUpdated = cartItem.copy(quantity = cartItem.quantity + 1)

                updateCartItemUseCase.invoke(cartItemUpdated)

                _cartUiState.update {
                    it.copy(
                        cartItems = it.cartItems.map { item ->
                            item.takeIf { it.product.id != cartItem.product.id } ?: item.copy(
                                quantity = cartItemUpdated.quantity
                            )
                        },
                    )
                }
                getCartItems()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun onDecreaseCartItem(cartItem: CartItem) {
        viewModelScope.launch() {
            try {
                val cartItemUpdated = cartItem.copy(quantity = cartItem.quantity - 1)

                updateCartItemUseCase.invoke(cartItemUpdated)

                _cartUiState.update {
                    it.copy(
                        cartItems = it.cartItems.map { item ->
                            item.takeIf { it.product.id != cartItem.product.id } ?: item.copy(
                                quantity = cartItemUpdated.quantity
                            )
                        },
                    )
                }
                getCartItems()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun onRemoveCartItem(cartItem: CartItem) {
        viewModelScope.launch() {
            try {
                deleteCartItem.invoke(cartItem.id)

                _cartUiState.update { state ->
                    val updatedCartItems = state.cartItems.filterNot {
                        it.product.id == cartItem.product.id
                    }
                    state.copy(cartItems = updatedCartItems)
                }
                getCartItems()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun getCartItems() {
        viewModelScope.launch {
            try {
                val allCartItems = getCartItemsUseCase.invoke()
                    ?: throw (throw NoSuchElementException("No se encontraron items en el carrito"))
                _cartUiState.update { state ->
                    state.copy(
                        cartItems = allCartItems,
                        screenState = ScreenState.Success("")
                    )
                }
                updateTotal()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            deleteCartItemsUseCase.invoke()
            _cartUiState.update { state ->
                state.copy(
                    cartItems = emptyList(),
                    subTotal = 0.0,
                    deliveryFee = 0.0,
                    totalAmount = 0.0
                )
            }
        }
    }

    private fun updateTotal() {
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

    private fun handleException(e: Exception) {
        val errorMessage = when (e) {
            is NoSuchElementException -> e.message ?: "Error de carga"
            else -> "Ocurrió un error inesperado"
        }
        _cartUiState.update { it.copy(screenState = ScreenState.Error(errorMessage)) }
        Log.e("com.iasiris.muniapp", "Error: ${e.message}")
    }
}

data class CartUiState(
    val screenState: ScreenState<String> = ScreenState.Loading,
    val FEE_PERCENTAGE: Double = 0.03,
    val cartItems: List<CartItem> = emptyList(),
    val subTotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val totalAmount: Double = 0.0,
    val isMaxItems: Boolean = false,
)