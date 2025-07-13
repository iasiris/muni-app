package com.iasiris.muniapp.data.remote

import com.iasiris.muniapp.data.remote.dto.OrderDto
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderHistoryApiService { //TODO
    @GET("orders/{userId}")
    suspend fun getOrderHistories(userId: String): List<OrderDto>

    @POST("orders")
    suspend fun postOrderHistory(): OrderDto
}