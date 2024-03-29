package com.livedata.covid19.vo


data class CountriesResponseItem(
    val active: Int,
    val cases: Int,
    val country: String,
    val countryInfo: CountryInfo,
    val critical: Int,
    val deaths: Int,
    val recovered: Int,
    val todayCases: Int,
    val todayDeaths: Int,
    val todayRecovered: Int
)