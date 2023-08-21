package com.example.assamplanetoriumscanner.model

data class ScannedTicket(
    val visitor_name: String,
    val booking_no: String,
    val mobile_no: String,
    val total_person: Int,
    val total_camera: Int,
    val net_amt: Double,
    val service_amt: Double,
    val total_amt: Double,
    val i_adult: Int,
    val i_child: Int,
    val i_student: Int,
    val f_adult: Int,
    val f_child: Int,
    val f_student: Int,
    val i_still: Int,
    val i_dslr: Int,
    val i_video: Int,
    val f_still: Int,
    val f_dslr: Int,
    val f_video: Int,
)