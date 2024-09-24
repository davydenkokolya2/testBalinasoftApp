package com.example.testbalinasoftapp.data.models

data class ApiResponse<T>(
    val status: Int,
    val data: T
)
