package com.livedata.covid19.vo


import com.google.gson.annotations.SerializedName

data class CountryInfo(
    val flag: String,
    @SerializedName("_id")
    val id: Int,
    val iso2: String,
    val iso3: String,
    val lat: Float,
    val long: Float
)