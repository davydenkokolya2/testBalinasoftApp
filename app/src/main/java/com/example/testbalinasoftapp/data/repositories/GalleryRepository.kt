package com.example.testbalinasoftapp.data.repositories

import android.util.Log
import com.example.testbalinasoftapp.data.api.ApiService
import com.example.testbalinasoftapp.data.database.AppDatabase
import com.example.testbalinasoftapp.data.models.ImageDeleteResponse
import com.example.testbalinasoftapp.data.models.ImageEntity
import com.example.testbalinasoftapp.data.models.ImageItem
import com.example.testbalinasoftapp.data.models.ImageUploadRequest
import com.example.testbalinasoftapp.data.models.ImageUploadResponse

class GalleryRepository(
    private val apiService: ApiService,
    private val database: AppDatabase
) {

    suspend fun getImagesFromApi(token: String, page: Int): List<ImageItem> {
        val response = apiService.getImages(page, token)
        if (response.status == 200) {
            return response.data
        } else {
            throw Exception("Error fetching images")
        }
    }

    suspend fun getImagesFromDb(): List<ImageEntity> {
        return database.imageDao().getAllImages()
    }

    suspend fun saveImagesToDb(images: List<ImageItem>) {
        val entities = images.map { imageItem ->
            ImageEntity(
                id = imageItem.id,
                url = imageItem.url,
                date = imageItem.date,
                lat = imageItem.lat,
                lng = imageItem.lng
            )
        }
        database.imageDao().insertAll(entities)
    }

    suspend fun uploadImageToApi(
        token: String,
        imageUploadRequest: ImageUploadRequest
    ): ImageUploadResponse {
        val response = apiService.uploadImage(token, imageUploadRequest)
        if (response.status == 200) {
            return response.data
        } else {
            throw Exception("Error fetching images")
        }
    }

    suspend fun deleteImage(imageId: Int, token: String): ImageDeleteResponse? {
        try {
            val response = apiService.deleteImage(imageId, token)
            if (response.isSuccessful && response.body()?.status == 200) {
                return response.body()?.data
            } else {
                throw Exception("Error deleting image")
            }
        } catch (e: Exception) {
            Log.e("DeleteImage", "Error: ${e.message}")
            return null
        }
    }

    suspend fun deleteFromDB(imageId: Int) {
        database.imageDao().deleteImageById(imageId)
    }
}
