package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class CarerSignupReuestModel(

    @field:SerializedName("first_name")
    val firstName: String? = null,

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("country_code")
    val countryCode: String? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("confirm_password")
    val confirmPassword: String? = null,

    // Patient fields
    @field:SerializedName("patient_first_name")
    val patientFirstName: String? = null,

    @field:SerializedName("patient_last_name")
    val patientLastName: String? = null,

    @field:SerializedName("patient_email")
    val patientEmail: String? = null,

    @field:SerializedName("patient_country_code")
    val patientCountryCode: String? = null,

    @field:SerializedName("patient_phone_number")
    val patientPhoneNumber: String? = null,

    @field:SerializedName("patient_language")
    val patientLanguage: String? = null
)


//data class CarerSignupReuestModel(
//
//	@field:SerializedName("password")
//	val password: String? = null,
//
//	@field:SerializedName("patient_phone_number")
//	val patientPhoneNumber: String? = null,
//
//	@field:SerializedName("full_name")
//	val fullName: String? = null,
//
//	@field:SerializedName("patient_email")
//	val patientEmail: String? = null,
//
//	@field:SerializedName("patient_language")
//	val patientLanguage: String? = null,
//
//	@field:SerializedName("patient_full_name")
//	val patientFullName: String? = null,
//	@field:SerializedName("patient_country_code")
//	val patient_country_code :String?=null,
//	@field:SerializedName("email")
//	val email: String? = null,
//
//	@field:SerializedName("confirm_password")
//	val confirmPassword: String? = null
//)
