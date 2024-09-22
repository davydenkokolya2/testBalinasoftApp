package com.example.testbalinasoftapp.ui.viewmodel

import GalleryItem
import androidx.lifecycle.ViewModel
import com.example.testbalinasoftapp.domain.FragmentType
import com.example.testbalinasoftapp.domain.ToolbarIconState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PhotoDetailViewModel : ViewModel() {
    private val _currentImage = MutableStateFlow<GalleryItem?>(null)
    val currentImage: MutableStateFlow<GalleryItem?> = _currentImage

    fun updateCurrentImage(item: GalleryItem) {
        _currentImage.value = item
    }
}