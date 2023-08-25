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
    @POST("scan-ticket")
    fun sendBookingDataToServer(
        @Header("token") token: String,
        @Field("data") bookingNumber: String
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("update-scandata")
    fun updateBookingDataToServer(
        @Header("token") token: String,
        @Field("data") bookingNumber: String
    ): Call<JsonObject>



    //GET SCANNED OVERALL TICKET LIST
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("overall-scan")
    fun getOverallScanData(
        @Header("token") token: String?,
        @Field("page") count: Int?
    ): Call<JsonObject>

    //get daily scans
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("daily-scan")
    fun getDailyScanData(
        @Header("token") token: String?,
        @Field("page") count: Int?
    ): Call<JsonObject>





}