package com.iasiris.muniapp.view.viewmodel

import androidx.lifecycle.SavedStateHandle
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
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productDataSource: ProductDataSource
) : ViewModel() {
    private val productId: String =
        checkNotNull(savedStateHandle["id"]) //TODO chequear si es el modo correcto

    private val _prodDetailUiState = MutableStateFlow(ProductDetailUiState())
    val prodDetailUiState: StateFlow<ProductDetailUiState> = _prodDetailUiState

    fun init() {
        getProduct()
    }

    fun onAdd() {
        val currentQuantity = _prodDetailUiState.value.product.quantity
        if (currentQuantity < _prodDetailUiState.value.MAX_QUANTITY) {
            _prodDetailUiState.update { state ->
                val newQuantity = currentQuantity + 1
                state.copy(
                    product = state.product.copy(quantity = newQuantity),
                    totalAmount = newQuantity * state.product.price
                )

            }
        } else {
            //TODO mostrar notificacion de error
        }
    }

    fun onRemove() {
        val currentQuantity = _prodDetailUiState.value.product.quantity
        if (currentQuantity > _prodDetailUiState.value.MIN_QUANTITY) {
            _prodDetailUiState.update { state ->
                val newQuantity = currentQuantity - 1
                state.copy(
                    product = state.product.copy(quantity = newQuantity),
                    totalAmount = newQuantity * state.product.price
                )
            }
        } else {
            //TODO mostrar notificacion de error
        }
    }

    fun onAddToCart() {
        //TODO agregar Implementación
    }

    private fun getProduct() {
        viewModelScope.launch {
            val product = withContext(Dispatchers.IO) {
                productDataSource.getProductById(productId)
            }
            product?.let {
                _prodDetailUiState.update { state ->
                    state.copy(product = product)
                }
            }
        }
    }
}

data class ProductDetailUiState(
    val product: Product = Product("", "", "", "", 0.0, false, ""),
    val MAX_QUANTITY: Int = 10,
    val MIN_QUANTITY: Int = 1,
    val totalAmount: Double = 0.0
)