package com.livedata.covid19.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

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
//                val gson = GsonBuilder().create()
//                val countriesData : List<CountriesResponse> = gson.fromJson(it.toString(), Array<CountriesResponse>::class.java).toList()
                }, {
                    _networkState.postValue(NetworkState.ERROR)
                    Log.e("FetchCountryDetails", it.message.toString())
                })
            )

        } catch (e: Exception) {

        }
    }
}


