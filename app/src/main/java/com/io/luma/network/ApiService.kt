package com.io.luma.network


import com.io.luma.model.SignupRequestModel
import com.io.luma.model.SignupResponseModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("v1/auth/signup")
    suspend fun createUser(@Body user: SignupRequestModel): Response<SignupResponseModel>
}
