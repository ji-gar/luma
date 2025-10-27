package com.io.luma.respositry

import com.io.luma.model.CarerSignupReuestModel
import com.io.luma.model.SignupResponseModel
import com.io.luma.model.VerifyNumberRequestModel
import com.io.luma.model.VerifyNumberResponseModel
import com.io.luma.network.ApiClient
import com.io.luma.network.Resource

class VerifyNumberRespositry {

    private val api = ApiClient.api

    suspend fun verifyNumber(user: VerifyNumberRequestModel): Resource<VerifyNumberResponseModel> {
        return try {
            val response = api.verifyPhone(user)
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