package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class InviatePaintentResponse(

	@field:SerializedName("invitation_id")
	val invitationId: Int? = null,

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("expires_at")
	val expiresAt: String? = null,

	@field:SerializedName("send_via_email")
	val sendViaEmail: Boolean? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("send_via_phone")
	val sendViaPhone: Boolean? = null,

	@field:SerializedName("is_used")
	val isUsed: Boolean? = null
)
