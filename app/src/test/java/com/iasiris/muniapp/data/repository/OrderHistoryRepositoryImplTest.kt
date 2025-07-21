package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.OrderHistoryLocalDataSource
import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.local.entity.OrderItemWithProductEntity
import com.iasiris.muniapp.data.remote.datasource.OrderHistoryRemoteDataSource
import com.iasiris.muniapp.utils.CommonUtils
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.utils.mockCartItem
import com.iasiris.muniapp.utils.mockOrderDto
import com.iasiris.muniapp.utils.mockOrderEntity
import com.iasiris.muniapp.utils.mockOrderItemEntity
import com.iasiris.muniapp.utils.mockProductEntity
import com.iasiris.muniapp.utils.mockUser
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderHistoryRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var orderHistoryRepository: OrderHistoryRepositoryImpl
    private val remoteDataSource: OrderHistoryRemoteDataSource = mockk()
    private val localDataSource: OrderHistoryLocalDataSource = mockk()
    private val commonUtils: CommonUtils = mockk()

    @Before
    fun setup() {
        orderHistoryRepository =
            OrderHistoryRepositoryImpl(remoteDataSource, localDataSource, commonUtils)
    }

    @Test
    fun getOrderHistoryByUserIdWithRefreshEqualsTrueReturnsRemoteOrders() = runTest {
        val userId = "user123"
        val mockRemoteOrders = listOf(mockOrderDto())
        coEvery { remoteDataSource.getOrderHistoryByUserId(userId) } returns mockRemoteOrders
        coEvery { localDataSource.getOrderHistory() } returns emptyList()
        coEvery { localDataSource.deleteOrderHistory() } just Runs
        coEvery { localDataSource.insertOrder(any()) } returns mockOrderEntity()
        coEvery { localDataSource.insertOrderItems(any(), any()) } returns emptyList()

        val result = orderHistoryRepository.getOrderHistoryByUserId(userId, refreshData = true)

        assertEquals(1, result.size)
        assertEquals(mockRemoteOrders.first().id, result.first().id)
    }

    @Test
    fun getOrderHistoryByUserIdWithoutRefreshReturnsLocalOrdersWhenAvailable() = runTest {
        val userId = "user123"
        val mockLocalOrder = OrderEntity(id = 1, totalAmount = 100.0, orderDate = "2023-01-01")
        val mockOrderItems = listOf(mockOrderItemEntity())

        coEvery { localDataSource.getOrderHistory() } returns listOf(mockLocalOrder)
        coEvery { localDataSource.getOrderItemsWithProductsByOrderId(1) } returns mockOrderItems.map {
            OrderItemWithProductEntity(it, mockProductEntity())
        }

        val result = orderHistoryRepository.getOrderHistoryByUserId(userId, refreshData = false)

        assertEquals(1, result.size)
        assertEquals("1", result.first().id)
        assertEquals(1, result.first().products.size)
    }

    @Test
    fun insertOrderReturnsCreatedOrder() = runTest {
        val userId = mockUser().id
        val cartItems = listOf(mockCartItem())
        val orderItemWithProductEntityList = listOf(
            OrderItemWithProductEntity(mockOrderItemEntity(), mockProductEntity())
        )

        coEvery { commonUtils.returnDate() } returns mockOrderEntity().orderDate
        coEvery { localDataSource.insertOrder(any()) } returns mockOrderEntity()
        coEvery { localDataSource.insertOrderItems(any(), any()) } returns orderItemWithProductEntityList
        coEvery { remoteDataSource.insertOrder(any()) } returns mockOrderDto().id

        val result = orderHistoryRepository.insertOrder(userId, cartItems)

        assertEquals("1", result.id)
        assertEquals(1, result.products.size)
        assertEquals(mockOrderEntity().totalAmount, result.totalAmount, 0.01)
    }


}