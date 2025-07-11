package com.iasiris.muniapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderHistoryDto(//TODO agregar userId para traer historial pedidos de usuario
    @SerializedName("_id")
    val orderId: String
)