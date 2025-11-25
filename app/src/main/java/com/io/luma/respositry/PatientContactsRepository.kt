package com.io.luma.respositry

import com.io.luma.model.PatientContactsResponseModel
import com.io.luma.network.ApiClient
import com.io.luma.network.Resource

class PatientContactsRepository {
    private val api = ApiClient.api

    suspend fun getPatientContacts(): Resource<PatientContactsResponseModel> {
        return try {
            val response = api.getPatientContacts()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network Error: ${e.localizedMessage}")
        }
    }
}