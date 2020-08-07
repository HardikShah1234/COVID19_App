package com.livedata.covid19.data.api

import com.livedata.covid19.vo.CoronaResponse
import com.livedata.covid19.vo.CountriesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/all/")
    fun getCoronaData(
        @Query("cases") cases: Int,
        @Query("active") active: Int
    ): Single<CoronaResponse>

    @GET("v2/countries/")
    fun getCountries(): Single<CountriesResponse>
}