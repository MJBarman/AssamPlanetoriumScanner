package com.example.assamplanetoriumscanner.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface Client {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("gate-login")
    fun login(
        @Field("mobile") mobile: String,
        @Field("password") password: String
    ) : Call<JsonObject>


    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("scan-data")
    fun sendBookingDataToServer(
        @Header("token") token: String,
        @Field("data") bookingNumber: String
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("update-scan-data")
    fun updateBookingDataToServer(
        @Header("token") token: String,
        @Field("data") bookingNumber: String
    ): Call<JsonObject>


    @Headers("Accept: application/json")
    @GET("get-daily-scan-data")
    fun getDailyScanData(
        @Header("token") token: String,
        @Query("page") count: Int
    ): Call<JsonObject>


    @Headers("Accept: application/json")
    @GET("get-overall-scan-data")
    fun getOverallScanData(
        @Header("token") token: String,
        @Query("page") count: Int
    ): Call<JsonObject>


}