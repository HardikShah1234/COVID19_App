package com.livedata.covid19.data.repository

import androidx.lifecycle.MutableLiveData
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import io.reactivex.disposables.CompositeDisposable
import javax.sql.DataSource

class CountryDataSourceFactory(
    private val apiService: ApiService,
    private val compositeDisposable: CompositeDisposable
) : androidx.paging.DataSource.Factory<Int, CountriesResponseItem>() {

    val countryLiveDataSource = MutableLiveData<CountryDataSource>()
    override fun create(): androidx.paging.DataSource<Int, CountriesResponseItem> {
        val countryDataSource = CountryDataSource(apiService, compositeDisposable)
        countryLiveDataSource.postValue(countryDataSource)
        return countryDataSource
    }

}