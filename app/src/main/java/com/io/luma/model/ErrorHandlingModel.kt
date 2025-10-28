package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class ErrorHandlingModel(

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
