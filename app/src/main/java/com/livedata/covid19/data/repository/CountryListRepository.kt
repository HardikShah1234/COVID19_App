package com.livedata.covid19.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import io.reactivex.disposables.CompositeDisposable

class CountryListRepository (private val apiService: ApiService) {

    lateinit var countryList: LiveData<PagedList<CountriesResponseItem>>
    lateinit var countryDataSourceFactory: CountryDataSourceFactory

    fun fetchLiveCountryList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<CountriesResponseItem>>
    {
        countryDataSourceFactory = CountryDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(20).build()

        countryList = LivePagedListBuilder(countryDataSourceFactory, config).build()
        return countryList
    }

    fun getNetworkState(): LiveData<NetworkState>{
        return Transformations.switchMap<CountryDataSource,NetworkState>(
            countryDataSourceFactory.countryLiveDataSource,CountryDataSource::networkState
        )
    }
}