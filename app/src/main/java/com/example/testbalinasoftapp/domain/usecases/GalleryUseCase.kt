package com.example.testbalinasoftapp.domain.usecases

import com.example.testbalinasoftapp.data.models.ImageDeleteResponse
import com.example.testbalinasoftapp.data.models.ImageItem
import com.example.testbalinasoftapp.data.models.ImageUploadRequest
import com.example.testbalinasoftapp.data.models.ImageUploadResponse
import com.example.testbalinasoftapp.data.repositories.GalleryRepository
import com.example.testbalinasoftapp.domain.Unix.toImageItem

class GalleryUseCase(private val repository: GalleryRepository) {

    suspend fun getImages(page: Int, token: String): List<ImageItem> {
        val imagesFromApi = repository.getImagesFromApi(token, page)
        repository.saveImagesToDb(imagesFromApi)
        return getLocalImages()
    }

    suspend fun getLocalImages(): List<ImageItem> {
        return repository.getImagesFromDb().map { it.toImageItem() }
    }

    suspend fun uploadImage(
        token: String,
        imageUploadRequest: ImageUploadRequest
    ): ImageUploadResponse {
        return repository.uploadImageToApi(token, imageUploadRequest)
    }

    suspend fun deleteImage(imageId: Int, token: String): ImageDeleteResponse? {
        deleteLocalImage(imageId)
        return repository.deleteImage(imageId, token)
    }

    suspend fun deleteLocalImage(imageId: Int) {
        repository.deleteFromDB(imageId)
    }
}

