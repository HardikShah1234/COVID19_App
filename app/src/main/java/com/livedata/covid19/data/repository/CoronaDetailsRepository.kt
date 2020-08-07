package com.livedata.covid19.data.repository

import androidx.lifecycle.LiveData
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.vo.CoronaResponse
import io.reactivex.disposables.CompositeDisposable

class CoronaDetailsRepository(private val apiService: ApiService) {

    lateinit var networkDataSource: NetworkDataSource

    fun fetchDetails(
        compositeDisposable: CompositeDisposable,
        cases: Int,
        active: Int
    ): LiveData<CoronaResponse> {
        networkDataSource = NetworkDataSource(apiService, compositeDisposable)
        networkDataSource.fetchCoronaDetails(cases, active)

        return networkDataSource.downlodedCoronaResponse
    }

    fun getCoronaDetailsNetworkState(): LiveData<NetworkState> {
        return networkDataSource.networkState
    }
}