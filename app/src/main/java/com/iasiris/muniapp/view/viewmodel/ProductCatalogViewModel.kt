package com.iasiris.muniapp.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import com.iasiris.muniapp.domain.model.Product
import com.iasiris.muniapp.domain.usecase.product.GetProductsUseCase
import com.iasiris.muniapp.view.ui.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@HiltViewModel
class ProductCatalogViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _prodCatUiState = MutableStateFlow(ProductCatalogUiState())
    val prodCatUiState: StateFlow<ProductCatalogUiState> = _prodCatUiState

    fun onCategorySelected(category: String) {
        _prodCatUiState.update { state ->
            val newCategory = if (state.selectedCategory == category) "" else category
            state.copy(
                selectedCategory = newCategory,
                products = filterProducts(state.searchText, newCategory, state.allProducts)
            )
        }
    }

    fun onSearchTextChange(text: String) {
        _prodCatUiState.update { state ->
            state.copy(
                searchText = text,
                products = filterProducts(text, state.selectedCategory, state.allProducts)
            )
        }
    }

    fun onOrderSelected(order: PriceOrder) {
        _prodCatUiState.update { state ->
            val sortedProducts = when (order) {
                PriceOrder.ASCENDING -> state.products.sortedBy { it.price }
                PriceOrder.DESCENDING -> state.products.sortedByDescending { it.price }
                PriceOrder.FEATURED -> filterProducts(
                    state.searchText,
                    state.selectedCategory,
                    state.allProducts
                )
            }
            state.copy(selectedOrder = order, products = sortedProducts)
        }
    }

    fun loadProducts(refreshData: Boolean = false) {
        //TODO!!!!! se borra cartItems cuando: catalog(agrega prod) -> cart(esta el prod) -> catalog -> cart(desaparece el prod)
        viewModelScope.launch {
            _prodCatUiState.update { it.copy(screenState = ScreenState.Loading) }
            try {
                val products = withContext(Dispatchers.IO) { getProductsUseCase.invoke(refreshData) }
                val categories = products.map { it.category }
                    .distinct()
                    .sorted()
                _prodCatUiState.update { state ->
                    state.copy(
                        allProducts = products,
                        products = filterProducts(
                            state.searchText,
                            state.selectedCategory,
                            products
                        ),
                        categories = categories,
                        screenState = ScreenState.Success(products),
                    )
                }
            } catch (e: IOException) {
                _prodCatUiState.update { it.copy(screenState = ScreenState.Error("Sin conexión a internet")) }
                Log.e("com.iasiris.muniapp", "Error de red: ${e.message}")
            } catch (e: HttpException) {
                _prodCatUiState.update { it.copy(screenState = ScreenState.Error("Error de servidor")) }
                Log.e("com.iasiris.muniapp", "Error HTTP: ${e.message}")
            } catch (e: Exception) {
                _prodCatUiState.update { it.copy(screenState = ScreenState.Error("Ocurrió un error inesperado")) }
                Log.e("com.iasiris.muniapp", "Error inesperado: ${e.message}")
            }
        }
    }

    private fun filterProducts(
        searchText: String,
        selectedCategory: String,
        allProducts: List<Product>
    ): List<Product> {
        return allProducts.filter { product ->
            val matchesSearch = product.name.contains(searchText, ignoreCase = true)
            val matchesCategory = selectedCategory.isBlank() || product.category.equals(
                selectedCategory,
                ignoreCase = true
            )
            matchesSearch && matchesCategory
        }
    }
}

data class ProductCatalogUiState(
    val screenState: ScreenState<List<Product>> = ScreenState.Loading,
    val allProducts: List<Product> = emptyList(),
    val products: List<Product> = emptyList(),
    val searchText: String = "",
    val isSearching: Boolean = false,
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "",
    val selectedOrder: PriceOrder = PriceOrder.FEATURED,
)

enum class PriceOrder {
    FEATURED,
    ASCENDING,
    DESCENDING
}