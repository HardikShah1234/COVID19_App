package com.livedata.covid19.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.livedata.covid19.R
import com.livedata.covid19.data.api.ApiClient
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.data.repository.CountryListRepository
import com.livedata.covid19.data.repository.CustomAdapter
import com.livedata.covid19.data.repository.NetworkState
import com.livedata.covid19.models.CountriesViewModel
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_countries.*

@Suppress("UNCHECKED_CAST")
class Countries : AppCompatActivity() {


    private lateinit var viewModel: CountriesViewModel
    lateinit var countryListRepository: CountryListRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)

        val apiService : ApiService = ApiClient.getClient()
        countryListRepository = CountryListRepository(apiService)
        viewModel = getViewModel()
        val countryAdapter = CustomAdapter(this)
        val gridLayoutManager = GridLayoutManager(this,3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = countryAdapter.getItemViewType(position)
                if (viewType == countryAdapter.VIEW_TYPE) return 1
                else return 3
            }
        }

        rv_countries.layoutManager = gridLayoutManager
        rv_countries.setHasFixedSize(true)
        rv_countries.adapter = countryAdapter

        viewModel.countryList.observe(this, Observer {
            countryAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            country_loader.visibility = if (viewModel.listisEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listisEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listisEmpty()) {
                countryAdapter.setNetworkState(it)
            }
        })


    }

    private fun getViewModel(): CountriesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CountriesViewModel(countryListRepository) as T
            }
        })[CountriesViewModel::class.java]

    }

}