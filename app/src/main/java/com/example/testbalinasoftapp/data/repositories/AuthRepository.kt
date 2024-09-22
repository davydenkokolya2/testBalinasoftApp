package com.example.testbalinasoftapp.data.repositories

import com.example.testbalinasoftapp.data.api.ApiService
import com.example.testbalinasoftapp.data.database.AppDatabase
import com.example.testbalinasoftapp.data.models.LoginRequest
import com.example.testbalinasoftapp.data.models.LoginResponse
import com.example.testbalinasoftapp.data.models.UserEntity
import retrofit2.Call

class AuthRepository(
    private val apiService: ApiService,
    private val database: AppDatabase
) {

    fun login(loginRequest: LoginRequest): Call<LoginResponse> {
        return apiService.login(loginRequest)
    }

    fun register(registerRequest: LoginRequest): Call<LoginResponse> {
        return apiService.register(registerRequest)
    }

    suspend fun saveUser(loginResponse: LoginResponse) {
        val user = UserEntity(
            userId = loginResponse.data.userId,
            login = loginResponse.data.login,
            token = loginResponse.data.token
        )
        database.userDao().insertUser(user)
    }
}
