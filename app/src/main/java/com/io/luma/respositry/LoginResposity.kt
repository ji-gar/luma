package com.io.luma.respositry

import com.io.luma.model.CarerSignupReuestModel
import com.io.luma.model.LoginRequestModel
import com.io.luma.model.SignupResponseModel
import com.io.luma.model.VerifyNumberResponseModel
import com.io.luma.network.ApiClient
import com.io.luma.network.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginResposity {
    private val api = ApiClient.api



    suspend fun loginUser(user: LoginRequestModel): Resource<VerifyNumberResponseModel> {
        return try {
            val response = api.loginUser(user)
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