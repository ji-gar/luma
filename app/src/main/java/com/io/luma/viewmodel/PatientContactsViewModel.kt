package com.io.luma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.luma.model.PatientContactsResponseModel
import com.io.luma.network.Resource
import com.io.luma.respositry.PatientContactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PatientContactsViewModel : ViewModel() {
    private val repository = PatientContactsRepository()

    val patientContacts = MutableStateFlow<Resource<PatientContactsResponseModel>?>(null)

    fun getPatientContacts() {
        viewModelScope.launch {
            patientContacts.value = Resource.Loading()
            patientContacts.value = repository.getPatientContacts()
        }
    }
}