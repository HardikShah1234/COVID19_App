package com.livedata.covid19.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.livedata.covid19.data.repository.CoronaDetailsRepository
import com.livedata.covid19.data.repository.NetworkState
import com.livedata.covid19.vo.CoronaResponse
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel (private val coronaRepository : CoronaDetailsRepository, cases:Int, active:Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val coronaDetails : LiveData<CoronaResponse> by lazy {
        coronaRepository.fetchDetails(compositeDisposable, cases, active)
    }

    val networkState:LiveData<NetworkState> by lazy {
        coronaRepository.getCoronaDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}