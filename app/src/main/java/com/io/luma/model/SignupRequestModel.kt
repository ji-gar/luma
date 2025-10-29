package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class SignupRequestModel(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("role")
	val role: String? = null,
	@field:SerializedName("country_code")
    val country_code : String ?=null,
	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("confirm_password")
	val confirmPassword: String? = null
)
