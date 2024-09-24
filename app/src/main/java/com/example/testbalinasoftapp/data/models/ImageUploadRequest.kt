package com.example.testbalinasoftapp.data.models

data class ImageUploadRequest(
    val base64Image: String,
    val date: Long,
    val lat: Double,
    val lng: Double
)
