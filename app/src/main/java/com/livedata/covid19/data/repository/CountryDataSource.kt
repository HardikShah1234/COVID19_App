package com.livedata.covid19.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.vo.CountriesResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CountryDataSource(
    private val apiService: ApiService,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedCountriesDataResponse = MutableLiveData<CountriesResponse>()
    val downloadedCountryResponse: LiveData<CountriesResponse>
        get() = _downloadedCountriesDataResponse

    fun fetchCountryDetails() {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getCountries().subscribeOn(Schedulers.io()).subscribe({
                    _downloadedCountriesDataResponse.postValue(it)
                    _networkState.postValue(NetworkState.LOADED)
                }, {
                    _networkState.postValue(NetworkState.ERROR)
                    Log.e("FetchCountryDetails", it.message.toString())
                })
            )

        } catch (e: Exception) {

        }
    }
}


