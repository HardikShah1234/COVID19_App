package com.livedata.covid19.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.livedata.covid19.data.repository.CountryDetailsRepository
import com.livedata.covid19.data.repository.NetworkState
import com.livedata.covid19.vo.CountriesResponse
import io.reactivex.disposables.CompositeDisposable

class CountriesViewModel(private val countryDetailsRepository: CountryDetailsRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val countryDetails: LiveData<CountriesResponse> by lazy {
        countryDetailsRepository.fetchCountryDetailsProperly(compositeDisposable)
    }
    val networkState: LiveData<NetworkState> by lazy {
        countryDetailsRepository.getCountryDetailsNetworkState()
    }

    fun listisEmpty(): Boolean {
        return true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}