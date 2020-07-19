package com.livedata.covid19.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.vo.CoronaResponse
import com.livedata.covid19.vo.CountriesResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NetworkDataSource (private val apiService: ApiService, private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState : LiveData<NetworkState>
        get() =_networkState

    //Downloaded data to display for Main Activity
    private val _downloadedCoronaDataResponse = MutableLiveData<CoronaResponse>()
    val downlodedCoronaResponse : LiveData<CoronaResponse>
        get() =_downloadedCoronaDataResponse

    fun fetchCoronaDetails(cases : Int, active: Int){
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getCoronaData(cases, active).subscribeOn(Schedulers.io()).subscribe({
                    _downloadedCoronaDataResponse.postValue(it)
                    _networkState.postValue(NetworkState.LOADED)
                }, {
                    _networkState.postValue(NetworkState.ERROR)
                    Log.e("CoronaDataSource", it.message.toString())
                })
            )

        } catch (e:Exception){
            Log.e("Not Proper",e.message.toString())
        }
    }
}