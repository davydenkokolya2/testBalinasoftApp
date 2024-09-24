package com.example.testbalinasoftapp.data.models

data class ImageUploadResponse(
    val id: Int,
    val url: String,
    val date: Long,
    val lat: Double,
    val lng: Double
)