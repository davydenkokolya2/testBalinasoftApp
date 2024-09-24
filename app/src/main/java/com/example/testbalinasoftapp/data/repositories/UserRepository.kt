package com.example.testbalinasoftapp.data.repositories

import com.example.testbalinasoftapp.data.database.AppDatabase
import com.example.testbalinasoftapp.data.models.LoginResponse
import com.example.testbalinasoftapp.data.models.UserEntity

class UserRepository(private val database: AppDatabase) {

    suspend fun saveUser(loginResponse: LoginResponse) {
        val user = UserEntity(
            userId = loginResponse.data.userId,
            login = loginResponse.data.login,
            token = loginResponse.data.token
        )
        database.userDao().insertUser(user)
    }

    suspend fun getUser(userId: Int): UserEntity? {
        return database.userDao().getUserById(userId)
    }
}