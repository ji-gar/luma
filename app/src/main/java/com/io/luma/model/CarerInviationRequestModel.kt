package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class CarerInviationRequestModel(

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("send_via")
	val sendVia: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
