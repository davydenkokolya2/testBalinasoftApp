package com.example.testbalinasoftapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: Int,
    val login: String,
    val token: String
)
