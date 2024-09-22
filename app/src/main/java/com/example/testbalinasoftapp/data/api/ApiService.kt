package com.example.testbalinasoftapp.data.api

import com.example.testbalinasoftapp.data.models.LoginRequest
import com.example.testbalinasoftapp.data.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/account/signin")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/account/signup")
    fun register(@Body request: LoginRequest): Call<LoginResponse>
}
