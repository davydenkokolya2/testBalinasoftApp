package com.example.testbalinasoftapp.data.models

data class LoginResponse(
    val status: Int,
    val data: UserData
)
data class UserData(
    val userId: Int,
    val login: String,
    val token: String
)