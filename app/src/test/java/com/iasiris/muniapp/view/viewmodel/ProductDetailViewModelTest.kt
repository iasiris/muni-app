package com.iasiris.muniapp.view.viewmodel

import com.iasiris.muniapp.data.local.ProductDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class ProductDetailViewModelTest { //TODO terminar este test
    private val testDispatcher = StandardTestDispatcher()
    private val mockProductDataSource = mock(ProductDataSource::class.java)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetProductDetails() {
        // Implement the test logic here
        // This is a placeholder for the actual test implementation
    }


}