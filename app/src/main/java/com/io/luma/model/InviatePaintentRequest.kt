package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class InviatePaintentRequest(

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("send_via_email")
	val sendViaEmail: Boolean? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("send_via_phone")
	val sendViaPhone: Boolean? = null
)
