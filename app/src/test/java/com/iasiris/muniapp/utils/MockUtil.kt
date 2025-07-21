package com.iasiris.muniapp.utils

import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.data.local.entity.CartItemWithProductEntity
import com.iasiris.muniapp.data.local.entity.OrderEntity
import com.iasiris.muniapp.data.local.entity.OrderItemEntity
import com.iasiris.muniapp.data.local.entity.ProductEntity
import com.iasiris.muniapp.data.remote.dto.OrderDto
import com.iasiris.muniapp.data.remote.dto.OrderItemDto
import com.iasiris.muniapp.data.remote.dto.ProductDto
import com.iasiris.muniapp.domain.model.CartItem
import com.iasiris.muniapp.domain.model.Order
import com.iasiris.muniapp.domain.model.OrderItem
import com.iasiris.muniapp.domain.model.Product
import com.iasiris.muniapp.domain.model.User

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

fun mockProductDto(
    id: String = "prod1",
    name: String = "Pizza",
    description: String = "Pizza grande muzzarella",
    imageUrl: String = "http://example.com/pizza.png",
    price: Double = 1500.0,
    hasDrink: Boolean = false,
    category: String = "Comida"
): ProductDto = ProductDto(id, name, description, imageUrl, price, hasDrink, category)

fun mockProductEntity(
    id: String = "prod1",
    name: String = "Pizza",
    description: String = "Pizza grande muzzarella",
    imageUrl: String = "http://example.com/pizza.png",
    price: Double = 1500.0,
    hasDrink: Boolean = false,
    category: String = "Comida"
): ProductEntity = ProductEntity(id, name, description, imageUrl, price, hasDrink, category)

fun mockOrderDto(
    id: String = "1",
    userId: String = "user123",
    products: List<OrderItemDto> = listOf(mockOrderItemDto()),
    totalAmount: Double = 100.0,
    orderDate: String = "2023-01-01"
): OrderDto = OrderDto(id, userId, products, totalAmount, orderDate)

fun mockOrderItemDto(
    product: ProductDto = mockProductDto(),
    quantity: Int = 2
): OrderItemDto = OrderItemDto(product, quantity)

fun mockOrderEntity(
    id: Int = 1,
    totalAmount: Double = 100.0,
    orderDate: String = "2023-01-01"
): OrderEntity = OrderEntity(id, totalAmount, orderDate)

fun mockOrderItemEntity(
    id: Int = 1,
    orderId: String = "1",
    productId: String = "prod1",
    quantity: Int = 2
): OrderItemEntity = OrderItemEntity(id, orderId, productId, quantity)

fun mockCartItemEntity(
    id: Int = 1,
    productId: String = "prod1",
    quantity: Int = 1
): CartItemEntity = CartItemEntity(id, productId, quantity)

fun mockCartItemWithProductEntity(
    cartItem: CartItemEntity = mockCartItemEntity(),
    product: ProductEntity = mockProductEntity()
): CartItemWithProductEntity = CartItemWithProductEntity(cartItem, product)