package com.io.luma.network


import com.io.luma.model.CarerSignupReuestModel
import com.io.luma.model.InviatePaintentRequest
import com.io.luma.model.InviatePaintentResponse
import com.io.luma.model.LoginRequestModel
import com.io.luma.model.LoginResponse
import com.io.luma.model.SetPasswordRequest
import com.io.luma.model.SignupRequestModel
import com.io.luma.model.SignupResponseModel
import com.io.luma.model.VerifyNumberRequestModel
import com.io.luma.model.VerifyNumberResponseModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("v1/auth/signup")
    suspend fun createUser(@Body user: SignupRequestModel): Response<SignupResponseModel>

    @POST("v1/carer/signup")
    suspend fun createcarerUser(@Body user: CarerSignupReuestModel): Response<SignupResponseModel>



    @POST("v1/auth/verify-phone")
    suspend fun verifyPhone(@Body user: VerifyNumberRequestModel): Response<VerifyNumberResponseModel>
    @POST("v1/auth/login-phone")
    suspend fun loginUser(@Body user: LoginRequestModel): Response<LoginResponse>

    @POST("v1/carer/invite-patient")
    suspend fun inviatePaintent(@Body user: InviatePaintentRequest): Response<InviatePaintentResponse>

    @POST("v1/auth/set-password")
    suspend fun setPassword(@Body user: SetPasswordRequest): Response<LoginResponse>
}
