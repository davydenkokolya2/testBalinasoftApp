package com.example.testbalinasoftapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey val id: Int,
    val url: String,
    val date: Long,
    val lat: Double,
    val lng: Double
)
