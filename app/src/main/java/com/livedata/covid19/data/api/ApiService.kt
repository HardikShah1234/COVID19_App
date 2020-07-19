package com.livedata.covid19.data.api

import android.telecom.Call
import com.livedata.covid19.vo.CoronaResponse
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService{

    @GET("v2/all/")
    fun getCoronaData(@Query("cases") cases : Int, @Query("active") active: Int): Single<CoronaResponse>

    @GET("v2/countries/")
    fun getCountries(@Query("country") country : String): Single<CountriesResponseItem>
}