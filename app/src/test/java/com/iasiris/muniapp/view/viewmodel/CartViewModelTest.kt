package com.iasiris.muniapp.view.viewmodel

import com.iasiris.muniapp.domain.usecase.cartitem.AddCartItemUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.DeleteCartItemUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.DeleteCartItemsUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.GetCartItemByProductIdUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.GetCartItemsUseCase
import com.iasiris.muniapp.domain.usecase.cartitem.UpdateCartItemUseCase
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.utils.mockCartItem
import com.iasiris.muniapp.utils.mockProduct
import com.iasiris.muniapp.view.ui.screen.ScreenState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCartItemsUseCase: GetCartItemsUseCase = mockk()
    private val getCartItemByProductIdUseCase: GetCartItemByProductIdUseCase = mockk()
    private val addCartItemUseCase: AddCartItemUseCase = mockk()
    private val updateCartItemUseCase: UpdateCartItemUseCase = mockk()
    private val deleteCartItem: DeleteCartItemUseCase = mockk()
    private val deleteCartItemsUseCase: DeleteCartItemsUseCase = mockk()

    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        viewModel = CartViewModel(
            getCartItemsUseCase,
            getCartItemByProductIdUseCase,
            addCartItemUseCase,
            updateCartItemUseCase,
            deleteCartItem,
            deleteCartItemsUseCase
        )
    }

    @Test
    fun addToCartWithNewProductAddsItemToCart() = runTest {
        val product = mockProduct()
        val newCartItem = mockCartItem()
        coEvery { getCartItemByProductIdUseCase.invoke(product.id) } returns null
        coEvery { addCartItemUseCase.invoke(product.id) } returns newCartItem

        viewModel.addToCart(product)
        advanceUntilIdle()

        val state = viewModel.cartUiState.value
        assertEquals(1, state.cartItems.size)
        assertEquals(newCartItem, state.cartItems[0])
        assertTrue(state.screenState is ScreenState.Success)
    }

    @Test
    fun addToCartWithExistingProductIncreasesQuantity() = runTest {
        val product = mockProduct()
        val existingCartItem = mockCartItem(quantity = 1)
        val updatedCartItem = existingCartItem.copy(quantity = 2)

        coEvery { getCartItemByProductIdUseCase.invoke(product.id) } returns existingCartItem
        coEvery { updateCartItemUseCase.invoke(updatedCartItem) } just Runs
        coEvery { getCartItemsUseCase.invoke() } returns listOf(updatedCartItem)

        viewModel.addToCart(product)
        advanceUntilIdle()

        val state = viewModel.cartUiState.value
        assertEquals(2, state.cartItems.first().quantity)
    }

    @Test
    fun onDecreaseCartItemUpdatesQuantityCorrectly() = runTest {
        val cartItem = mockCartItem(quantity = 2)
        val updatedCartItem = cartItem.copy(quantity = 1)
        coEvery { updateCartItemUseCase.invoke(updatedCartItem) } returns Unit
        coEvery { getCartItemsUseCase.invoke() } returns listOf(updatedCartItem)

        viewModel.onDecreaseCartItem(cartItem)
        advanceUntilIdle()

        val state = viewModel.cartUiState.value
        assertEquals(1, state.cartItems.first().quantity)
    }

    @Test
    fun onRemoveCartItemRemovesItemFromCart() = runTest {
        val cartItem = mockCartItem()
        coEvery { deleteCartItem.invoke(cartItem.id) } returns Unit
        coEvery { getCartItemsUseCase.invoke() } returns emptyList()

        viewModel.onRemoveCartItem(cartItem)
        advanceUntilIdle()

        val state = viewModel.cartUiState.value
        assertTrue(state.cartItems.isEmpty())
        coVerify { deleteCartItem.invoke(cartItem.id) }
    }

    @Test
    fun clearCartEmptiesCartAndResetsTotals() = runTest {
        coEvery { deleteCartItemsUseCase.invoke() } returns Unit

        viewModel.clearCart()
        advanceUntilIdle()

        val state = viewModel.cartUiState.value
        assertTrue(state.cartItems.isEmpty())
        assertEquals(0.0, state.totalAmount, 0.0)
    }
}