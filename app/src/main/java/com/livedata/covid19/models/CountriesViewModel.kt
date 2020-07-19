package com.livedata.covid19.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.livedata.covid19.data.repository.CountryListRepository
import com.livedata.covid19.data.repository.NetworkState
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import io.reactivex.disposables.CompositeDisposable

class CountriesViewModel (private val countryListRepository: CountryListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val countryList: LiveData<PagedList<CountriesResponseItem>> by lazy {
        countryListRepository.fetchLiveCountryList(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        countryListRepository.getNetworkState()
    }

    fun listisEmpty() : Boolean {
        return countryList.value?.isEmpty()?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}