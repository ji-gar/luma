package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class SetPasswordRequest(

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("confirm_password")
	val confirmPassword: String? = null
)
