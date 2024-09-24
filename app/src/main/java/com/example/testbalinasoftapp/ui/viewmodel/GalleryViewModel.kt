package com.example.testbalinasoftapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testbalinasoftapp.data.models.ImageItem
import com.example.testbalinasoftapp.data.models.ImageUploadRequest
import com.example.testbalinasoftapp.domain.usecases.GalleryUseCase

class GalleryViewModel(private val galleryUseCase: GalleryUseCase) : ViewModel() {

    private val _images = MutableLiveData<MutableList<ImageItem>>(mutableListOf())
    val images: MutableLiveData<MutableList<ImageItem>> get() = _images

    suspend fun fetchImages(token: String) {
        _images.value = galleryUseCase.getImages(0, token).toMutableList()
    }

    suspend fun fetchLocalImages(): MutableList<ImageItem> {
        return galleryUseCase.getLocalImages().toMutableList()
    }

    suspend fun uploadImage(token: String, imageUploadRequest: ImageUploadRequest) {
        galleryUseCase.uploadImage(token, imageUploadRequest)
        fetchImages(token)
    }

    suspend fun deleteImage(imageId: Int, token: String) {
        galleryUseCase.deleteImage(imageId, token)
        fetchImages(token)
    }
}
