package com.iasiris.muniapp.ui.screen.productcatalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iasiris.muniapp.data.local.ProductDataSource
import com.iasiris.muniapp.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductCatalogViewModel @Inject constructor(
    private val productDataSource: ProductDataSource
) : ViewModel() {

    private val _prodCatUiState = MutableStateFlow(ProductCatalogUiState())
    val prodCatUiState: StateFlow<ProductCatalogUiState> = _prodCatUiState

    init {
        getProducts()
    }

    fun onCategorySelected(category: String) {//TODO FIX THIS
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

    private fun getProducts() {
        viewModelScope.launch {
            val allProducts = productDataSource.getProducts()
            _prodCatUiState.update { state ->
                state.copy(
                    allProducts = allProducts,
                    products = filterProducts(state.searchText, state.selectedCategory, allProducts)
                )
            }
        }
    }

    private fun filterProducts(
        searchText: String,
        selectedCategory: String,
        allProducts: List<Product>
    ): List<Product> { //FIX THIS
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
    val allProducts: List<Product> = emptyList(),
    val products: List<Product> = emptyList(),
    val searchText: String = "",
    val isSearching: Boolean = false,
    val categories: List<String> = listOf(
        "Hamburguesas",
        "Tacos",
        "Empanadas"
    ), //TODO TAKE THIS FROM API
    val selectedCategory: String = "",
    val selectedOrder: PriceOrder = PriceOrder.FEATURED
)

enum class PriceOrder {
    FEATURED,
    ASCENDING,
    DESCENDING
}