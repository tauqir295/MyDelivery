package com.my.delivery.datasource

import com.my.delivery.deliverylist.model.Delivery
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/v2/deliveries/?")
    fun getDeliveries(@Query("offset") offset : Int, @Query("limit") limit : Int): Call<ArrayList<Delivery>>
}