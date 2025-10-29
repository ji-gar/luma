package com.io.luma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.luma.model.SignupRequestModel
import com.io.luma.model.SignupResponseModel
import com.io.luma.model.User
import com.io.luma.network.Resource
import com.io.luma.respositry.PatientSignupRepositry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repository = PatientSignupRepositry()
    private val _createUser = MutableStateFlow<Resource<SignupResponseModel>?>(null)
    val createUser: StateFlow<Resource<SignupResponseModel>?> = _createUser
    var user by mutableStateOf(SignupRequestModel())
        private set

    fun updateName(name: String) {
        user = user.copy(fullName = name)
    }

    fun updatePhone(phone: String) {
        user = user.copy(phoneNumber = "$phone")
    }

    fun updateEmail(email: String) {
        user = user.copy(email = email)
    }

    fun updatePassword(password: String, confirm: String) {
        user = user.copy(password = password, confirmPassword = confirm)
    }

    fun updateRole(role: String) {
        user = user.copy(role = role)
    }

    fun updateLanguage(language: String) {
        user = user.copy(language = language)
    }
    fun updateCountryCode(code: String) {
        user = user.copy(country_code = code)
    }

    fun addDetils(userSignupRequestModel: SignupRequestModel)
    {
        viewModelScope.launch {

            _createUser.value= Resource.Loading()
            _createUser.value= repository.createUser(userSignupRequestModel)
        }

    }
}
