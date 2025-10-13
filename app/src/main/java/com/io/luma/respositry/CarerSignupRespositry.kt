package com.io.luma.respositry

import com.io.luma.model.CarerSignupReuestModel
import com.io.luma.model.SignupRequestModel
import com.io.luma.model.SignupResponseModel
import com.io.luma.network.ApiClient
import com.io.luma.network.Resource

class CarerSignupRespositry {

    private val api = ApiClient.api


    suspend fun createUser(user: CarerSignupReuestModel): Resource<SignupResponseModel> {
        return try {
            val response = api.createcarerUser(user)
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