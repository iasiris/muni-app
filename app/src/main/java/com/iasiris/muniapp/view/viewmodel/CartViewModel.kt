package com.iasiris.muniapp.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Product
import com.iasiris.muniapp.domain.usecase.cartitem.AddCartItemUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.DeleteCartItemsUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.GetCartItemByProductIdUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.GetCartItemsUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.UpdateCartItemUseCase
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
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val getCartItemByProductIdUseCase: GetCartItemByProductIdUseCase,
    private val addCartItemUseCase: AddCartItemUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val deleteCartItemsUseCase: DeleteCartItemsUseCase
) : ViewModel() {
    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState

    fun init() {
        getCartItems()
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val existingCartItem = withContext(Dispatchers.IO) {
                getCartItemByProductIdUseCase.invoke(product.id)
            }
            if (existingCartItem == null) {
                val newCartItem = withContext(Dispatchers.IO) {
                    addCartItemUseCase.invoke(product)
                }
                _cartUiState.update { state ->
                    val updatedCartItems = state.cartItems + newCartItem
                    state.copy(cartItems = updatedCartItems)
                }
                updateTotal()//todo check this
            } else {
                onIncreaseCartItem(existingCartItem)
            }
        }
    }

    fun onIncreaseCartItem(cartItem: CartItem) {
        //todo update the quantity of the cart item in DB
        viewModelScope.launch() {
            val cartItemUpdated = cartItem.copy(quantity = cartItem.quantity + 1)
            if (cartItem.quantity < _cartUiState.value.MAX_CART_ITEM_QUANTITY) {

            }
            withContext(Dispatchers.IO) { //update en DB
                updateCartItemUseCase.invoke(cartItemUpdated)
            }
            getCartItems()
        }
    }

    fun onDecreaseCartItem(cartItem: CartItem) {
        viewModelScope.launch() {
            val cartItemUpdated = cartItem.copy(quantity = cartItem.quantity - 1)
            withContext(Dispatchers.IO) { //update en DB
                updateCartItemUseCase.invoke(cartItemUpdated)
            }
            getCartItems()
        }
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
            val allCartItems = withContext(Dispatchers.IO) { getCartItemsUseCase.invoke() }
            _cartUiState.update { state ->
                state.copy(cartItems = allCartItems)
            }
            updateTotal()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { deleteCartItemsUseCase.invoke() }
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