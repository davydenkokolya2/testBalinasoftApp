package com.example.testbalinasoftapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testbalinasoftapp.data.models.LoginRequest
import com.example.testbalinasoftapp.data.models.LoginResponse
import com.example.testbalinasoftapp.domain.usecases.LoginUseCase
import com.example.testbalinasoftapp.domain.usecases.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val loginUseCase: LoginUseCase, private val userUseCase: UserUseCase) :
    ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    private val _token = MutableStateFlow("")
    val token: MutableStateFlow<String> = _token

    fun login(login: String, password: String) {
        loginUseCase.login(LoginRequest(login, password)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {

                    viewModelScope.launch {
                        userUseCase.saveUser(response.body()!!)
                    }
                    _loginResult.value = Result.success(response.body()!!)
                    _token.value = response.body()!!.data.token
                } else {
                    _loginResult.value =
                        Result.failure(Throwable("Login failed: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginResult.value = Result.failure(t)
            }
        })
    }

    fun register(login: String, password: String) {
        loginUseCase.register(LoginRequest(login, password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        viewModelScope.launch {
                            userUseCase.saveUser(response.body()!!)
                        }
                        _loginResult.value = Result.success(response.body()!!)
                        _token.value = response.body()!!.data.token
                    } else {
                        _loginResult.value =
                            Result.failure(Throwable("Login failed: ${response.message()}"))
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _loginResult.value = Result.failure(t)
                }
            })
    }
}
