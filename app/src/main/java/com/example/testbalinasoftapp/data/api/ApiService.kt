package com.example.testbalinasoftapp.data.api

import com.example.testbalinasoftapp.data.models.ApiResponse
import com.example.testbalinasoftapp.data.models.ImageDeleteResponse
import com.example.testbalinasoftapp.data.models.ImageItem
import com.example.testbalinasoftapp.data.models.ImageUploadRequest
import com.example.testbalinasoftapp.data.models.ImageUploadResponse
import com.example.testbalinasoftapp.data.models.LoginRequest
import com.example.testbalinasoftapp.data.models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/account/signin")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/account/signup")
    fun register(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/image")
    suspend fun getImages(
        @Query("page") page: Int,
        @Header("Access-Token") token: String
    ): ApiResponse<List<ImageItem>>

    @POST("api/image")
    suspend fun uploadImage(
        @Header("Access-Token") token: String,
        @Body imageUploadRequest: ImageUploadRequest
    ): ApiResponse<ImageUploadResponse>

    @DELETE("api/image/{id}")
    suspend fun deleteImage(
        @Path("id") imageId: Int,
        @Header("Access-Token") token: String
    ): Response<ApiResponse<ImageDeleteResponse>>
}
