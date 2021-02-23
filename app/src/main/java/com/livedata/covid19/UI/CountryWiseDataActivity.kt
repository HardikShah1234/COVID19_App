package com.livedata.covid19.UI

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.livedata.covid19.R
import com.livedata.covid19.data.api.ApiClient
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.data.repository.CountryDetailsRepository
import com.livedata.covid19.data.repository.CustomAdapter
import com.livedata.covid19.models.CountriesViewModel
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import kotlinx.android.synthetic.main.activity_country_wise_data.*
import kotlinx.android.synthetic.main.flag_list.*

class CountryWiseDataActivity : AppCompatActivity() {

    //private lateinit var viewModel: CountriesViewModel
    private lateinit var countryDetailsRepository: CountryDetailsRepository
    val countriesResponse = CountriesResponse()
    val customAdapter = CustomAdapter(this, countriesResponse)
    var countryList = countriesResponse


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_wise_data)
//        val apiService: ApiService = ApiClient.getClient()
//        countryDetailsRepository = CountryDetailsRepository(apiService)
//
//        viewModel = getViewModel()

        val intent = getIntent()
        val position = intent.getIntExtra("position", 0)
        tv_country.text = countryList.get(position).country
        tv_country_cases.text = countryList.get(position).cases.toString()
        tv_country_active.text = countryList.get(position).active.toString()
        tv_country_critical.text = countryList.get(position).critical.toString()
        tv_country_recovered.text = countryList.get(position).recovered.toString()
        tv_country_today_cases.text = countryList.get(position).todayCases.toString()
        tv_country_today_deaths.text = countryList.get(position).todayDeaths.toString()
        tv_country_total_deaths.text = countryList.get(position).deaths.toString()

//        viewModel.countryDetails.observe(this, Observer {
//                tv_country.text = it.get(position).country
//                tv_country_cases.text = it.get(position).cases.toString()
//                tv_country_active.text = it.get(position).active.toString()
//                tv_country_critical.text = it.get(position).critical.toString()
//                tv_country_recovered.text = it.get(position).recovered.toString()
//                tv_country_today_cases.text = it.get(position).todayCases.toString()
//                tv_country_today_deaths.text = it.get(position).todayDeaths.toString()
//                tv_country_total_deaths.text = it.get(position).deaths.toString()
//        })
    }

//    private fun getViewModel(): CountriesViewModel {
//        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return CountriesViewModel(countryDetailsRepository) as T
//            }
//        })[CountriesViewModel::class.java]
//
//    }
}