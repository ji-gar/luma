package com.io.luma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.luma.model.LoginRequestModel
import com.io.luma.model.LoginResponse
import com.io.luma.model.VerifyNumberResponseModel
import com.io.luma.network.Resource
import com.io.luma.respositry.LoginResposity
import com.io.luma.respositry.VerifyNumberRespositry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = LoginResposity()
    private val _loginState = MutableStateFlow<Resource<LoginResponse>?>(null)
    val loginState: StateFlow<Resource<LoginResponse>?> = _loginState

    fun addDetils(userSignupRequestModel: LoginRequestModel)
    {
        viewModelScope.launch {

            _loginState.value= Resource.Loading()
            _loginState.value= repository.loginUser(userSignupRequestModel)
        }

    }
}