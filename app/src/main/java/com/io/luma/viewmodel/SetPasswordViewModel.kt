package com.io.luma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.luma.model.LoginResponse
import com.io.luma.model.SetPasswordRequest
import com.io.luma.network.Resource
import com.io.luma.respositry.SetPasswordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SetPasswordViewModel : ViewModel() {

    private val repository = SetPasswordRepository()

    private val _setPasswordState = MutableStateFlow<Resource<LoginResponse>?>(null)
    val setPasswordState: StateFlow<Resource<LoginResponse>?> = _setPasswordState

    fun setPassword(request: SetPasswordRequest) {
        viewModelScope.launch {
            _setPasswordState.value = Resource.Loading()
            _setPasswordState.value = repository.setPassword(request)
        }
    }

    fun resetSetPasswordState() {
        _setPasswordState.value = null
    }
}