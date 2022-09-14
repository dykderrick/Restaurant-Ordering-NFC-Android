package com.app.Instaros.api

import com.google.gson.annotations.SerializedName

data class RestResponce<T>(
    @field:SerializedName("code")
    var code: Int? = null,

    @field:SerializedName("err")
    var err: String? = null,

    @field:SerializedName("payload")
    var payload: T? = null,

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("message")
    var message: String? = null
)