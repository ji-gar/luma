package com.io.luma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.luma.model.CarerSignupReuestModel
import com.io.luma.model.SignupRequestModel
import com.io.luma.model.SignupResponseModel
import com.io.luma.network.Resource
import com.io.luma.respositry.CarerSignupRespositry
import com.io.luma.respositry.PatientSignupRepositry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarerRegisterViewModel : ViewModel()
{
    private val repository = CarerSignupRespositry()
    private val _createUser = MutableStateFlow<Resource<SignupResponseModel>?>(null)
    val createUser: StateFlow<Resource<SignupResponseModel>?> = _createUser

    var user by mutableStateOf(CarerSignupReuestModel())
        private set


    fun updateFullName(name: String) {
        user = user.copy(fullName = name)
    }

    fun updateEmail(email: String) {
        user = user.copy(email = email)
    }

    fun updatePassword(password: String, confirmPassword: String) {
        user = user.copy(password = password, confirmPassword = confirmPassword)
    }

    fun updatePatientName(patientName: String) {
        user = user.copy(patientFullName = patientName)
    }
    fun updatePatientEmail(patientEmail: String) {
        user = user.copy(patientEmail = patientEmail)
    }

    fun updatePatientPhone(patientPhone: String) {
        user = user.copy(patientPhoneNumber = patientPhone)
    }

    fun updatePatientLanguage(language: String) {
        user = user.copy(patientLanguage = language)
    }


    fun addDetils(userSignupRequestModel: CarerSignupReuestModel)
    {
        viewModelScope.launch {

            _createUser.value= Resource.Loading()
            _createUser.value= repository.createUser(userSignupRequestModel)
        }

    }
}