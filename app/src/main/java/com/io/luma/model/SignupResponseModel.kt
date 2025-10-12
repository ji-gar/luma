package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class SignupResponseModel(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: Int? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: Any? = null,

	@field:SerializedName("is_email_verified")
	val isEmailVerified: Boolean? = null,

	@field:SerializedName("profile_picture_url")
	val profilePictureUrl: Any? = null,

	@field:SerializedName("avatar_gender")
	val avatarGender: Any? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("completion_stage")
	val completionStage: Int? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("location")
	val location: Any? = null,

	@field:SerializedName("known_as")
	val knownAs: Any? = null,

	@field:SerializedName("email")
	val email: String? = null
)
