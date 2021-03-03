package com.livedata.covid19.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.livedata.covid19.R
import com.livedata.covid19.data.api.ApiClient
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.data.repository.CountryDetailsRepository
import com.livedata.covid19.data.repository.CustomAdapter
import com.livedata.covid19.models.CountriesViewModel
import com.livedata.covid19.vo.CountriesResponse
import kotlinx.android.synthetic.main.activity_country_wise_data.*

class CountryWiseDataActivity : AppCompatActivity() {

    private lateinit var viewModel: CountriesViewModel
    private lateinit var countryDetailsRepository: CountryDetailsRepository
    //lateinit var countryAdapter: CustomAdapter
    private val countriesResponse = ArrayList<CountriesResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_wise_data)

        val apiService: ApiService = ApiClient.getClient()
        countryDetailsRepository = CountryDetailsRepository(apiService)

        viewModel = getViewModel()


        viewModel.countryDetails.observe(this, Observer {
            bindUI(it)
        })
    }

    fun bindUI(it: CountriesResponse) {
        val intent = getIntent()
        val position = intent.getIntExtra("position", 0)
        tv_country_cases.text = it.get(position).cases.toString()
        tv_country_active.text = it.get(position).active.toString()
        tv_country_critical.text = it.get(position).critical.toString()
        tv_country_recovered.text = it.get(position).recovered.toString()
        tv_country_today_cases.text = it.get(position).todayCases.toString()
        tv_country_today_deaths.text = it.get(position).todayDeaths.toString()
        tv_country_total_deaths.text = it.get(position).deaths.toString()
    }

    private fun getViewModel(): CountriesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CountriesViewModel(countryDetailsRepository) as T
            }
        })[CountriesViewModel::class.java]

    }
}