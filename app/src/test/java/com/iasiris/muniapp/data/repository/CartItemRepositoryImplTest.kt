package com.iasiris.muniapp.data.repository

import com.iasiris.muniapp.data.local.datasource.CartItemLocalDataSource
import com.iasiris.muniapp.utils.MainDispatcherRule
import com.iasiris.muniapp.utils.mockCartItem
import com.iasiris.muniapp.utils.mockCartItemEntity
import com.iasiris.muniapp.utils.mockCartItemWithProductEntity
import com.iasiris.muniapp.utils.mockProduct
import com.iasiris.muniapp.utils.mockProductEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CartItemRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var cartItemRepository: CartItemRepositoryImpl
    private val localDataSource: CartItemLocalDataSource = mockk()

    @Before
    fun setup() {
        cartItemRepository = CartItemRepositoryImpl(localDataSource)
    }

    @Test
    fun getCartItemsReturnsMappedDomainItems() = runTest {
        val mockEntities = listOf(
            mockCartItemEntity(id = 1, productId = "prod1"),
            mockCartItemEntity(id = 2, productId = "prod2")
        )
        coEvery { localDataSource.getCartItems() } returns mockEntities

        val result = cartItemRepository.getCartItems()

        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("prod1", result[0].product.id)
        assertEquals("2", result[1].id)
        assertEquals("prod2", result[1].product.id)
    }

    @Test
    fun getCartItemsWithProductsReturnsMappedItemsWithProducts() = runTest {
        val mockItems = listOf(
            mockCartItemWithProductEntity(
                cartItem = mockCartItemEntity(id = 1, productId = "prod1"),
                product = mockProductEntity(id = "prod1")
            )
        )
        coEvery { localDataSource.getCartItemsWithProducts() } returns mockItems

        val result = cartItemRepository.getCartItemsWithProducts()

        assertNotNull(result)
        assertEquals(1, result?.size)
        assertEquals("prod1", result?.first()?.product?.id)
    }

    @Test
    fun getCartItemByProductIdReturnsMappedItem() = runTest {
        val mockItem = mockCartItemWithProductEntity(
            cartItem = mockCartItemEntity(id = 1, productId = mockProduct().id),
            product = mockProductEntity(id = mockProduct().id)
        )
        coEvery { localDataSource.getCartItemByProductId(mockProduct().id) } returns mockItem

        val result = cartItemRepository.getCartItemByProductId(mockProduct().id)

        assertNotNull(result)
        assertEquals(mockProduct().id, result?.product?.id)
    }

    @Test
    fun insertCartItemReturnsMappedDomainItem() = runTest {
        val mockItem = mockCartItemWithProductEntity(
            cartItem = mockCartItemEntity(),
            product = mockProductEntity()
        )
        coEvery { localDataSource.insertCartItem(any()) } returns mockItem

        val result = cartItemRepository.insertCartItem(mockProduct().id)

        assertEquals("1", result.id)
        assertEquals(mockProduct().id, result.product.id)
    }

    @Test
    fun updateCartItemCompletesSuccessfully() = runTest {
        val cartItem = mockCartItem(id = "1", product = mockProduct(id = "prod1"))
        coEvery { localDataSource.updateCartItem(any()) } returns Unit

        cartItemRepository.updateCartItem(cartItem)

        assertTrue(true)
    }

    @Test
    fun deleteCartItemCompletesSuccessfully() = runTest {
        val cartItemId = "1"
        coEvery { localDataSource.deleteCartItem(cartItemId) } returns Unit

        cartItemRepository.deleteCartItem(cartItemId)

        assertTrue(true)
    }

    @Test
    fun deleteCartItemsCompletesSuccessfully() = runTest {
        coEvery { localDataSource.deleteCartItems() } returns Unit

        cartItemRepository.deleteCartItems()

        assertTrue(true)
    }

}