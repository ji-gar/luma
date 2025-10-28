package com.io.luma.respositry

import android.util.Log
import com.google.gson.Gson
import com.io.luma.model.ErrorHandlingModel
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
                Log.d("Else",response.errorBody()?.string().toString())
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorHandlingModel::class.java)
                Resource.Error("${errorResponse.message}")
            }
        } catch (e: Exception) {
            Log.d("Else","Catch")
            Resource.Error("Something went wrong")
        }
    }
}