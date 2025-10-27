package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class VerifyNumberRequestModel(
	@SerializedName("country_code")
	val countryCode: String? = null,

	@SerializedName("phone_number")
	val phoneNumber: String? = null
)

