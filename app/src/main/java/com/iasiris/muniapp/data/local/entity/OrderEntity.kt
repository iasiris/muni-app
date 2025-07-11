package com.iasiris.muniapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, //TODO no deberia ser int?
    val orderDate: Long = System.currentTimeMillis(),
    val totalAmount: Double,
    val totalItems: Int
)

