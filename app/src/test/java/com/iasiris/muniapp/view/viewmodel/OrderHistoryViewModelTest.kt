package com.iasiris.muniapp.view.viewmodel

import com.iasiris.muniapp.data.local.UserPreferences
import com.iasiris.muniapp.domain.usecase.orderhistory.AddOrderUseCase
import com.iasiris.muniapp.domain.usecase.orderhistory.GetOrdersByUserIdUseCase
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.utils.mockCartItem
import com.iasiris.muniapp.utils.mockOrder
import com.iasiris.muniapp.view.ui.screen.ScreenState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderHistoryViewModelTest {
    private val getOrdersByUserIdUseCase: GetOrdersByUserIdUseCase = mockk()
    private val addOrderUseCase: AddOrderUseCase = mockk()
    private var userPreferences: UserPreferences = mockk()

    private lateinit var viewModel: OrderHistoryViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        every { userPreferences.userIdFlow } returns flowOf("user123")

        viewModel = OrderHistoryViewModel(
            getOrdersByUserIdUseCase,
            addOrderUseCase,
            userPreferences
        )
    }

    @Test
    fun loadOrderHistoryUpdatesStateWithOrders() = runTest {
        val mockOrders = listOf(mockOrder())
        coEvery { getOrdersByUserIdUseCase.invoke("user123", any()) } returns mockOrders

        viewModel.loadOrderHistory()
        advanceUntilIdle()

        val state = viewModel.orderHistoryUiState.value
        assertEquals(mockOrders, state.orderHistory)
        assertTrue(state.screenState is ScreenState.Success)
    }

    @Test
    fun addOrderUpdatesStateWithNewOrder() = runTest {
        val cartItems = listOf(mockCartItem())
        val newOrder = mockOrder()
        coEvery { addOrderUseCase.invoke("user123", cartItems) } returns newOrder
        coEvery { getOrdersByUserIdUseCase.invoke("user123", any()) } returns listOf(newOrder)

        viewModel.addOrder(cartItems)
        advanceUntilIdle()

        val state = viewModel.orderHistoryUiState.value
        assertTrue(state.orderHistory.contains(newOrder))
        assertTrue(state.isOrderAdded)
        assertTrue(state.screenState is ScreenState.Success)
    }
}