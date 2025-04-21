package com.contraentrega.ceapp.model

data class UpdateOrderRequest(
    val idClient: Int,
    val orderNumber: Int,
    val idStatus: Int,
    val serviceType: Int
)