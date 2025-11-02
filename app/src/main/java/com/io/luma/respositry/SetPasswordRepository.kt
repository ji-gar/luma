package com.io.luma.respositry

import com.io.luma.model.LoginResponse
import com.io.luma.model.SetPasswordRequest
import com.io.luma.network.ApiClient
import com.io.luma.network.Resource

class SetPasswordRepository {
    private val api = ApiClient.api
    suspend fun setPassword(request: SetPasswordRequest): Resource<LoginResponse> {
        return try {
            val response = api.setPassword(request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Unknown error")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }
}