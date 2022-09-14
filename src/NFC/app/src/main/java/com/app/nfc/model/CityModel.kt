package com.app.nfc.model

import com.google.gson.annotations.SerializedName

data class CityModel (
    @field:SerializedName("city_name")
    var cityName: String = "",

    @field:SerializedName("id")
    var cityid: String = "",

    @field:SerializedName("state_id")
    var stateId: String = ""
)