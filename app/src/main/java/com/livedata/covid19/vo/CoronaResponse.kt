package com.livedata.covid19.vo


data class CoronaResponse(
    val active: Int,
    val affectedCountries: Int,
    val cases: Int,
    val critical: Int,
    val deaths: Int,
    val recovered: Int,
    val todayCases: Int,
    val todayDeaths: Int,
    val todayRecovered: Int
)