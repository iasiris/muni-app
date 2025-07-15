package com.iasiris.muniapp.data.remote

import com.iasiris.muniapp.data.remote.dto.OrderDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path

interface OrderHistoryApiService { //TODO
    @GET("orders/{userId}")
    suspend fun getOrderHistory(@Path("userId")userId: String): List<OrderDto>

    @POST("orders")
    suspend fun postOrder(@Body orderDto: OrderDto)
}