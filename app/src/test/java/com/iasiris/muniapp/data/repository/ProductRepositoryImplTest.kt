package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.ProductLocalDataSource
import com.iasiris.muniapp.data.remote.datasource.ProductRemoteDataSource
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.utils.mockProduct
import com.iasiris.muniapp.utils.mockProductDto
import com.iasiris.muniapp.utils.mockProductEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var productRepository: ProductRepositoryImpl
    private val remoteDataSource: ProductRemoteDataSource = mockk()
    private val localDataSource: ProductLocalDataSource = mockk()

    @Before
    fun setup() {
        productRepository = ProductRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun getProductsWithRefreshEqualTrueFetchesFromRemote() = runTest {
        val mockRemoteProducts = listOf(mockProductDto())
        val mockLocalProducts = listOf(mockProductEntity())
        coEvery { remoteDataSource.getProducts() } returns mockRemoteProducts
        coEvery { localDataSource.clearProducts() } just Runs
        coEvery { localDataSource.insertProducts(mockLocalProducts) } just Runs

        val result = productRepository.getProducts(refreshData = true)

        assertEquals(1, result.size)
        assertEquals(mockRemoteProducts.first().id, result.first().id)
    }

    @Test
    fun getProductsWithoutRefreshFetchesFromLocal() = runTest {
        val mockLocalProducts = listOf(mockProductEntity())
        coEvery { localDataSource.getProducts() } returns mockLocalProducts


        val result = productRepository.getProducts(refreshData = false)

        assertEquals(1, result.size)
        assertEquals(mockLocalProducts.first().id, result.first().id)
    }


    @Test
    fun getProductsWithoutRefreshReturnsRemoteProductsWhenLocalIsEmpty() = runTest {
        val mockRemoteProducts = listOf(mockProductDto())
        val mockLocalProducts = listOf(mockProductEntity())
        coEvery { localDataSource.getProducts() } returns emptyList()
        coEvery { remoteDataSource.getProducts() } returns mockRemoteProducts
        coEvery { localDataSource.insertProducts(mockLocalProducts) } just Runs

        val result = productRepository.getProducts(refreshData = false)

        assertEquals(1, result.size)
        assertEquals(mockRemoteProducts.first().id, result.first().id)
    }

    @Test
    fun getProductByIdReturnsLocalProductWhenAvailable() = runTest {
        val productId = mockProduct().id
        val mockLocalProduct = mockProductEntity(id = productId)
        coEvery { localDataSource.getProductById(productId) } returns mockLocalProduct

        val result = productRepository.getProductById(productId)

        assertNotNull(result)
        assertEquals(productId, result?.id)
    }

    @Test
    fun getProductByIdReturnRemoteProductWhenLocalNotAvailable() = runTest {
        val productId = mockProduct().id
        val mockRemoteProduct = mockProductDto(id = productId)
        val mockLocalProduct = mockProductEntity()
        coEvery { localDataSource.getProductById(productId) } returns null
        coEvery { remoteDataSource.getProductById(productId) } returns mockRemoteProduct
        coEvery { localDataSource.insertProduct(mockLocalProduct) } just Runs

        val result = productRepository.getProductById(productId)

        assertNotNull(result)
        assertEquals(productId, result?.id)
    }

}