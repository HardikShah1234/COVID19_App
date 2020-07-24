package com.livedata.covid19.data.repository

import androidx.lifecycle.LiveData
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response

class CountryDetailsRepository(private val apiService: ApiService) {

    lateinit var countryDataSource: CountryDataSource

    fun fetchCountryDetailsProperly(compositeDisposable: CompositeDisposable): LiveData<CountriesResponse> {
        countryDataSource = CountryDataSource(apiService, compositeDisposable)
        countryDataSource.fetchCountryDetails()

        return countryDataSource.downloadedCountryResponse
    }

    fun getCountryDetailsNetworkState(): LiveData<NetworkState> {
        return countryDataSource.networkState
    }
}