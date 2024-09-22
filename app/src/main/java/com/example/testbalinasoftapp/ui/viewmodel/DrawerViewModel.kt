package com.example.testbalinasoftapp.ui.viewmodel

import androidx.lifecycle.ViewModel

import com.example.testbalinasoftapp.domain.usecases.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DrawerViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName

    suspend fun loadUserName(userId: Int) {
        _userName.value = userUseCase.getUser(userId)?.login ?: ""
    }
}