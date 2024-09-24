package com.example.testbalinasoftapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.testbalinasoftapp.data.models.ImageItem
import kotlinx.coroutines.flow.MutableStateFlow

class PhotoDetailViewModel : ViewModel() {
    private val _currentImage = MutableStateFlow<ImageItem?>(null)
    val currentImage: MutableStateFlow<ImageItem?> = _currentImage

    fun updateCurrentImage(item: ImageItem) {
        _currentImage.value = item
    }
}