package com.contraentrega.ceapp.network

import com.contraentrega.ceapp.model.AuthResponse
import retrofit2.http.*
import retrofit2.Call
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {
    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun login(
        @Header("Authorization") authorization: String = "Basic Y29udHJhZW50cmVnYWFwcDpjb250cmE2NGVudHJlZ2E=",
        @Field("grant_type") grantType: String = "password",
        @Field("username") username: String,
        @Field("password") password: String
    ): AuthResponse
}

interface ApiService {
    @PUT("orders/v1/update/{order_number}")
    fun updateOrder(@Path("order_number") orderNumber: String): Call<ApiResponse>
}

data class ApiResponse(
    val status: String
)