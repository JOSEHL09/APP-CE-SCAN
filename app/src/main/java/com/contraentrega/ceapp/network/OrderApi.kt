package com.contraentrega.ceapp.network

import com.contraentrega.ceapp.model.CountTodayResponse
import com.contraentrega.ceapp.model.UpdateOrderRequest
import com.contraentrega.ceapp.ui.qrscanner.QrViewModel.UpdateOrderResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderApi {
    @POST("orders/update-status")
    fun updateOrderStatus(@Body request: UpdateOrderRequest): Call<UpdateOrderResponse>

    @POST("orders/count-today")
    fun countTodayOrders(@Body request: UpdateOrderRequest): Call<CountTodayResponse>
}
