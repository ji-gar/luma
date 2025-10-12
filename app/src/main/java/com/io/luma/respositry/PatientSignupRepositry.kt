package com.io.luma.respositry

import com.io.luma.model.SignupRequestModel
import com.io.luma.model.SignupResponseModel
import com.io.luma.network.ApiClient
import com.io.luma.network.Resource

class  PatientSignupRepositry {
    private val api = ApiClient.api
    suspend fun createUser(user: SignupRequestModel): Resource<SignupResponseModel> {
        return try {
            val response = api.createUser(user)
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