package com.iasiris.muniapp.utils

import com.iasiris.muniapp.data.remote.dto.UserDto
import com.iasiris.muniapp.domain.mapper.userDtoToDomain
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.model.OrderItem
import com.iasiris.muniapp.domain.model.Product
import com.iasiris.muniapp.domain.model.User
import io.mockk.every
import io.mockk.mockk

fun mockUser(
    id: String = "user123",
    email: String = "test@test.com",
    password: String = "password123",
    fullName: String = "John Doe",
) = User(
    id = id,
    email = email,
    password = password,
    fullName = fullName
)

fun mockProduct(
    id: String = "prod1",
    name: String = "Pizza",
    description: String = "Pizza grande muzzarella",
    imageUrl: String = "http://example.com/pizza.png",
    price: Double = 1500.0,
    hasDrink: Boolean = false,
    category: String = "Comida"
) = Product(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    price = price,
    hasDrink = hasDrink,
    category = category
)

fun mockOrderItem(
    product: Product = mockProduct(),
    quantity: Int = 2
) = OrderItem(
    product = product,
    quantity = quantity
)

fun mockOrder(
    id: String = mockUser().id,
    userId: String = "user123",
    products: List<OrderItem> = listOf(mockOrderItem()),
    totalAmount: Double = 3000.0,
    orderDate: String = "2024-07-15"
) = Order(
    id = id,
    userId = userId,
    products = products,
    totalAmount = totalAmount,
    orderDate = orderDate
)

fun mockCartItem(
    id: String = "cart1",
    product: Product = mockProduct(),
    quantity: Int = 2
) = CartItem(
    id = id,
    product = product,
    quantity = quantity
)
