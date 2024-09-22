package com.example.testbalinasoftapp.domain.usecases

import com.example.testbalinasoftapp.data.models.LoginRequest
import com.example.testbalinasoftapp.data.models.LoginResponse
import com.example.testbalinasoftapp.data.repositories.AuthRepository
import retrofit2.Call

class LoginUseCase(private val authRepository: AuthRepository) {
    fun login(loginRequest: LoginRequest): Call<LoginResponse> {
        return authRepository.login(loginRequest)
    }

    fun register(loginRequest: LoginRequest): Call<LoginResponse> {
        return authRepository.register(loginRequest)
    }

}
