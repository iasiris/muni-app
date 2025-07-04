package com.iasiris.muniapp.ui.screen.orderhistory

import com.iasiris.muniapp.data.local.OrderDataSource
import com.iasiris.muniapp.data.model.Order
import com.iasiris.muniapp.view.viewmodel.OrderHistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class OrderHistoryViewModelTest {//TODO terminar este test
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetOrderHistory() = runTest {
        val orderHistoryMock = emptyList<Order>()

        val orderDataSourceMock = mock<OrderDataSource>()
        whenever(orderDataSourceMock.getOrdersByUserId("1")).thenReturn(orderHistoryMock)

        val viewModel = OrderHistoryViewModel(orderDataSourceMock)

        viewModel.getOrderHistory()
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.orderHistoryUiState.value.orders
        Assertions.assertEquals(orderHistoryMock, result)
    }
}