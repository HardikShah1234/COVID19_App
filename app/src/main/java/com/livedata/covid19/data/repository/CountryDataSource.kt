package com.livedata.covid19.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.data.api.FIRST_PAGE
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CountryDataSource(private val apiService: ApiService, private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, CountriesResponseItem>() {
    private var page = FIRST_PAGE
    private var country = ""
    val networkState : MutableLiveData<NetworkState> = MutableLiveData()
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CountriesResponseItem>
    ) {

        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(apiService.getCountries("country").subscribeOn(Schedulers.io()).subscribe({
            callback.onResult(ArrayList<CountriesResponseItem>(),0,null)
            networkState.postValue(NetworkState.LOADING)
        },{
            networkState.postValue(NetworkState.ERROR)
            Log.e("CountryDataSource" , it.message)
        }))
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CountriesResponseItem>
    ) {

    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CountriesResponseItem>
    ) {

    }
}