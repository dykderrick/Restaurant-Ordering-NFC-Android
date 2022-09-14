package com.app.Instaros.api

import com.google.gson.annotations.SerializedName

data class ListResponce<T> (
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("err")
    val err: String? = null,

    @field:SerializedName("payload")
    val payload: ArrayList<T> = ArrayList(),

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)