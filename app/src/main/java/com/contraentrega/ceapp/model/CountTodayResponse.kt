package com.contraentrega.ceapp.model

data class CountTodayResponse(
    val code: Int,
    val message: String,
    val totalOrders: Int  // Importante: mantener el nombre tal como viene de la API
)