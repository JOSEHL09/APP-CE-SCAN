package com.contraentrega.ceapp.model

data class AuthResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val scope: String
)
