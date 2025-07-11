package com.iasiris.muniapp.data.remote

import com.iasiris.muniapp.data.remote.dto.OrderHistoryDto
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderHistoryApiService { //TODO
    @GET("orders")
    suspend fun getOrderHistories(): List<OrderHistoryDto>

    @POST("orders")
    suspend fun postOrderHistory(): OrderHistoryDto
}