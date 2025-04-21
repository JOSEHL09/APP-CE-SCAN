package com.contraentrega.ceapp.network

import com.contraentrega.ceapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val BASE_URL_ORDERS = BuildConfig.BASE_URL_ORDERS
    private val BASE_URL_AUTH = BuildConfig.BASE_URL_AUTH

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofitOrders: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_ORDERS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val retrofitAuth: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_AUTH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofitAuth.create(AuthApi::class.java)
    }

    val orderApi: OrderApi by lazy {
        retrofitOrders.create(OrderApi::class.java)
    }
}