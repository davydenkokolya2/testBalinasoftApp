package com.example.testbalinasoftapp.data.repositories

import com.example.testbalinasoftapp.data.api.ApiService
import com.example.testbalinasoftapp.data.models.LoginRequest
import com.example.testbalinasoftapp.data.models.LoginResponse
import retrofit2.Call

class AuthRepository(
    private val apiService: ApiService
) {

    fun login(loginRequest: LoginRequest): Call<LoginResponse> {
        return apiService.login(loginRequest)
    }

    fun register(registerRequest: LoginRequest): Call<LoginResponse> {
        return apiService.register(registerRequest)
    }
}
