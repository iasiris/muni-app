package com.iasiris.muniapp.data.local.datasource

import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.model.Product
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
        id = 1,
        product = Product("1",
            "Tacos al pastor",
            "Tortillas con carne de cerdo marinada, pi\u00f1a y cebolla.",
            "https://comedera.com/wp-content/uploads/sites/9/2017/08/tacos-al-pastor-receta.jpg",
            45.0,
            true,
            "Tacos"),
        quantity = 2
    ),
    CartItem(
        id = 2,
        product = Product("1",
            "Tacos al pastor",
            "Tortillas con carne de cerdo marinada, pi\u00f1a y cebolla.",
            "https://comedera.com/wp-content/uploads/sites/9/2017/08/tacos-al-pastor-receta.jpg",
            45.0,
            true,
            "Tacos"),
        quantity = 1
    ),
    CartItem(
        id = 3,
        product = Product("1",
            "Tacos al pastor",
            "Tortillas con carne de cerdo marinada, pi\u00f1a y cebolla.",
            "https://comedera.com/wp-content/uploads/sites/9/2017/08/tacos-al-pastor-receta.jpg",
            45.0,
            true,
            "Tacos"),
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



