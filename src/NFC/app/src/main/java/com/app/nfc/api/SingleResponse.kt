package com.app.Instaros.api

import com.google.gson.annotations.SerializedName

data class SingleResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("payload")
	val payload: String? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
