package com.io.luma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.luma.model.SignupResponseModel
import com.io.luma.model.VerifyNumberRequestModel
import com.io.luma.model.VerifyNumberResponseModel
import com.io.luma.network.Resource
import com.io.luma.respositry.VerifyNumberRespositry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MobileNumberCheckViewModel : ViewModel() {
    private val repository = VerifyNumberRespositry()
    private val _verifyNumber = MutableStateFlow<Resource<VerifyNumberResponseModel>?>(null)
    val verifyNumber: StateFlow<Resource<VerifyNumberResponseModel>?> = _verifyNumber

    fun verifyNumber(verifyNumberRequestModel: VerifyNumberRequestModel)
    {
        viewModelScope.launch {

            _verifyNumber.value= Resource.Loading()
            _verifyNumber.value= repository.verifyNumber(verifyNumberRequestModel)
        }

    }
    fun resetInviteState() {
        _verifyNumber.value = null  // 👈 just clear the state instead of Idle
    }
}