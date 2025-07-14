package com.iasiris.muniapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.iasiris.muniapp.data.local.entity.CartItemEntity
import com.iasiris.muniapp.domain.model.CartItem

data class CartItemDto(
    @SerializedName("_id")
    val id: String,
    val product: ProductDto,
    val quantity: Int = 1
)

