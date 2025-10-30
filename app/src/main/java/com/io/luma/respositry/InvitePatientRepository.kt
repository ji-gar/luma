package com.io.luma.respositry

import android.util.Log
import com.google.gson.Gson
import com.io.luma.model.ErrorHandlingModel
import com.io.luma.model.InviatePaintentRequest
import com.io.luma.model.InviatePaintentResponse
import com.io.luma.network.ApiClient
import com.io.luma.network.Resource

class InvitePatientRepository {

    private val api = ApiClient.api

    suspend fun invitePatient(user: InviatePaintentRequest): Resource<InviatePaintentResponse> {
        return try {
            val response = api.inviatePaintent(user)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Log.d("InvitePatient", "Error: ${response.errorBody()?.string()}")
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorHandlingModel::class.java)
                Resource.Error("${errorResponse.message}")
            }
        } catch (e: Exception) {
            Log.e("InvitePatient", "Exception: ${e.localizedMessage}")
            Resource.Error("Something went wrong")
        }
    }
}
