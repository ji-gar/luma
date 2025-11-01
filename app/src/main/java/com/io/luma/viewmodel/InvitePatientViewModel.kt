package com.io.luma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.luma.model.InviatePaintentRequest
import com.io.luma.model.InviatePaintentResponse
import com.io.luma.network.Resource
import com.io.luma.respositry.InvitePatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InvitePatientViewModel : ViewModel() {

    private val repository = InvitePatientRepository()

    private val _invitePatientState = MutableStateFlow<Resource<InviatePaintentResponse>?>(null)
    val invitePatientState: StateFlow<Resource<InviatePaintentResponse>?> = _invitePatientState

    fun invitePatient(request: InviatePaintentRequest) {
        viewModelScope.launch {
            _invitePatientState.value = Resource.Loading()
            _invitePatientState.value = repository.invitePatient(request)
        }
    }
    fun resetInviteState() {
        _invitePatientState.value = null  // 👈 just clear the state instead of Idle
    }
}
