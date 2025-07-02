package com.iasiris.muniapp.data.local

import com.iasiris.muniapp.data.model.CartItem
import com.iasiris.muniapp.data.model.Order
import jakarta.inject.Inject

class OrderDataSourceImpl @Inject constructor() : OrderDataSource {
    override fun getOrdersByUserId(userId: String): List<Order> {
        return orderHistory
    }

    override fun updateOrder(order: Order): Boolean {
        TODO("Not yet implemented")
    }
}

private val cartItems = listOf(
    CartItem(
        name = "Hamburguesa Clásica",
        description = "Hamburguesa con carne, queso y lechuga",
        imageUrl = "https://ejemplo.com/hamburguesa.jpg",
        price = 50,
        hasDrink = true,
        quantity = 2
    ),
    CartItem(
        name = "Pizza Margarita",
        description = "Pizza con salsa de tomate, mozzarella y albahaca",
        imageUrl = "https://ejemplo.com/pizza.jpg",
        price = 70,
        hasDrink = false,
        quantity = 1
    ),
    CartItem(
        name = "Ensalada César",
        description = "Ensalada con pollo, lechuga y aderezo César",
        imageUrl = "https://ejemplo.com/ensalada.jpg",
        price = 40,
        hasDrink = false,
        quantity = 1
    )
)

private val orderHistory = listOf(
    Order(
        orderId = "1",
        productsId = listOf(cartItems[0], cartItems[2]),
        totalPrice = 140,
        orderDate = "01/06/2025"
    ),
    Order(
        orderId = "2",
        productsId = listOf(cartItems[1]),
        totalPrice = 70,
        orderDate = "05/06/2025"
    )
)



