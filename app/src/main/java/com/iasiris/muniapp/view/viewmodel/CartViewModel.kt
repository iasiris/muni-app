package com.iasiris.muniapp.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iasiris.muniapp.data.local.ProductDataSource
import com.iasiris.muniapp.data.model.Product
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
    private val productDataSource: ProductDataSource
) : ViewModel() {
    //TODO agregar CartDataSource para manejar la logica de negocio
    //TODO Usar datos que vengan de un repositorio o base de datos
    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState

    fun init() {
        getProducts()
    }

    fun onAddProduct(productAdd: Product) {
        _cartUiState.update { state ->
            val updatedProducts = state.products.map {
                if (it.name == productAdd.name && it.quantity < state.MAX_PRODUCT_QUANTITY) {
                    it.copy(quantity = it.quantity + 1)
                } else it
            }
            state.copy(products = updatedProducts)
        }
        updateTotal()
    }

    fun onRemoveProduct(productRemoved: Product) {
        _cartUiState.update { state ->
            val updatedProducts = state.products.mapNotNull {
                if (it.name == productRemoved.name && it.quantity > state.MIN_PRODUCT_QUANTITY) {
                    it.copy(quantity = it.quantity - 1)
                } else it
            }
            state.copy(products = updatedProducts)
        }
        updateTotal()
    }

    fun onDeleteProduct(productDeleted: Product) {
        _cartUiState.update { state ->
            val updatedProducts = state.products.filterNot {
                it.name == productDeleted.name
            }
            state.copy(products = updatedProducts)
        }
        updateTotal()
    }

    private fun getProducts() {
        viewModelScope.launch {
            val allProducts = withContext(Dispatchers.IO) {
                productDataSource.getProducts()
            }
            _cartUiState.update { state ->
                state.copy(products = allProducts)
            }
            updateTotal()
        }
    }

    private fun updateTotal() {
        _cartUiState.update { state ->
            val subTotal = state.products.sumOf { it.price * it.quantity }
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
    val MAX_PRODUCT_QUANTITY: Int = 10,
    val MIN_PRODUCT_QUANTITY: Int = 1,
    val FEE_PERCENTAGE: Double = 0.03,
    val products: List<Product> = emptyList(),
    val subTotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val totalAmount: Double = 0.0
)