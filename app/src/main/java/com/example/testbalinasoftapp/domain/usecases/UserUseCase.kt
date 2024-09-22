package com.example.testbalinasoftapp.domain.usecases

import com.example.testbalinasoftapp.data.models.LoginResponse
import com.example.testbalinasoftapp.data.models.UserEntity
import com.example.testbalinasoftapp.data.repositories.UserRepository

class UserUseCase(private val userRepository: UserRepository) {

    suspend fun saveUser(loginResponse: LoginResponse) {
        userRepository.saveUser(loginResponse)
    }

    suspend fun getUser(userId: Int): UserEntity? {
        return userRepository.getUser(userId)
    }
}