package com.iasiris.muniapp.view.viewmodel

import com.iasiris.muniapp.domain.usecase.product.GetProductsUseCase
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.utils.mockProduct
import com.iasiris.muniapp.view.ui.screen.ScreenState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductCatalogViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProductsUseCase: GetProductsUseCase = mockk()
    private lateinit var viewModel: ProductCatalogViewModel

    @Before
    fun setUp() {
        viewModel = ProductCatalogViewModel(getProductsUseCase)
    }

    @Test
    fun getProdCatUiState() = runTest {
        coEvery { getProductsUseCase.invoke(any()) } returns emptyList()

        val viewModel = ProductCatalogViewModel(getProductsUseCase)
        viewModel.loadProducts(true)
        advanceUntilIdle()
        val state = viewModel.prodCatUiState.value

        assertTrue(state.products.isEmpty())
        assertTrue(state.allProducts.isEmpty())
        assertTrue(state.categories.isEmpty())
        assertTrue(state.screenState is ScreenState.Success)
    }

    @Test
    fun onCategorySelectedFiltersProductsByCategory() = runTest {
        val products = listOf(
            mockProduct(id = "1", category = "Electronics"),
            mockProduct(id = "2", category = "Books"),
            mockProduct(id = "3", category = "Electronics")
        )
        coEvery { getProductsUseCase.invoke(any()) } returns products

        val viewModel = ProductCatalogViewModel(getProductsUseCase)
        viewModel.loadProducts(true)
        advanceUntilIdle()

        viewModel.onCategorySelected("Electronics")
        val state = viewModel.prodCatUiState.value

        assertEquals(2, state.products.size)
        assertTrue(state.products.all { it.category == "Electronics" })
    }


    @Test
    fun onSearchTextChangeFiltersProductsByName() = runTest {
        val products = listOf(
            mockProduct(id = "1", name = "Laptop"),
            mockProduct(id = "2", name = "Book"),
            mockProduct(id = "3", name = "Tablet")
        )
        coEvery { getProductsUseCase.invoke(any()) } returns products

        val viewModel = ProductCatalogViewModel(getProductsUseCase)
        viewModel.loadProducts(true)
        advanceUntilIdle()

        viewModel.onSearchTextChange("Lap")
        val state = viewModel.prodCatUiState.value
        assertEquals(1, state.products.size)
        assertEquals("Laptop", state.products[0].name)
    }

    @Test
    fun onOrderSelectedSortsBySelection() = runTest {
        val products = listOf(
            mockProduct(id = "1", price = 100.0),
            mockProduct(id = "2", price = 50.0),
            mockProduct(id = "3", price = 150.0)
        )
        coEvery { getProductsUseCase.invoke(any()) } returns products

        val viewModel = ProductCatalogViewModel(getProductsUseCase)
        viewModel.loadProducts(true)
        advanceUntilIdle()

        viewModel.onOrderSelected(PriceOrder.ASCENDING)
        val ascState = viewModel.prodCatUiState.value.products
        assertEquals(listOf("2", "1", "3"), ascState.map { it.id })

        viewModel.onOrderSelected(PriceOrder.DESCENDING)
        val descState = viewModel.prodCatUiState.value.products
        assertEquals(listOf("3", "1", "2"), descState.map { it.id })

        viewModel.onOrderSelected(PriceOrder.FEATURED)
        val featuredState = viewModel.prodCatUiState.value.products
        assertEquals(products.map { it.id }, featuredState.map { it.id })
    }

    @Test
    fun loadProducts() = runTest {
        coEvery { getProductsUseCase.invoke(any()) } returns listOf(
            mockProduct(id = "prod1"),
            mockProduct(id = "prod2")
        )

        val viewModel = ProductCatalogViewModel(getProductsUseCase)
        viewModel.loadProducts(true)
        advanceUntilIdle()
        val state = viewModel.prodCatUiState.value

        assertEquals(2, state.products.size)
        assertEquals("prod1", state.products[0].id)
        assertEquals("prod2", state.products[1].id)
        assertEquals(2, state.allProducts.size)
        assertEquals("prod1", state.allProducts[0].id)
        assertEquals("prod2", state.allProducts[1].id)
        assertTrue(state.categories.isNotEmpty())
        assertTrue(state.screenState is ScreenState.Success)
    }
}