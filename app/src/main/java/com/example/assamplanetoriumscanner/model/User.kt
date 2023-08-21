package com.example.assamplanetoriumscanner.model

data class User(
    val user_id: Int,
    val role_id: Int,
    val token: String? = "",
    var mobile_no: String? = "",
    val name: String,
    var terminal_id: String? = ""
)
