package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class CarerSignupReuestModel(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("patient_phone_number")
	val patientPhoneNumber: String? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("patient_email")
	val patientEmail: String? = null,

	@field:SerializedName("patient_language")
	val patientLanguage: String? = null,

	@field:SerializedName("patient_full_name")
	val patientFullName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("confirm_password")
	val confirmPassword: String? = null
)
