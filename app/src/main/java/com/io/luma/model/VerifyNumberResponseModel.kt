package com.io.luma.model


import com.google.gson.annotations.SerializedName

data class VerifyNumberResponseModel(

	@SerializedName("status")
	val status: Boolean? = null,

	@SerializedName("data")
	val data: Data? = null,

	@SerializedName("message")
	val message: String? = null
)

data class Data(

	@SerializedName("stage")
	val stage: Int? = null,

	@SerializedName("next_action")
	val nextAction: String? = null,

	@SerializedName("user")
	val user: User? = null
)






