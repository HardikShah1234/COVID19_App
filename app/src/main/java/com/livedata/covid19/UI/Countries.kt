package com.livedata.covid19.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.GsonBuilder
import com.livedata.covid19.R
import com.livedata.covid19.data.api.ApiClient
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.data.repository.CountryDetailsRepository
import com.livedata.covid19.data.repository.CustomAdapter
import com.livedata.covid19.data.repository.NetworkState
import com.livedata.covid19.models.CountriesViewModel
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import kotlinx.android.synthetic.main.activity_countries.*
import okhttp3.ResponseBody
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class Countries : AppCompatActivity() {


    private lateinit var viewModel: CountriesViewModel
    private lateinit var countryDetailsRepository: CountryDetailsRepository
    lateinit var countryAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)

        val apiService: ApiService = ApiClient.getClient()
        countryDetailsRepository = CountryDetailsRepository(apiService)

        viewModel = getViewModel()
        val gridLayoutManager = GridLayoutManager(this, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = countryAdapter.getItemViewType(position)
                if (viewType == countryAdapter.VIEW_TYPE) return 3
                else return 1
            }
        }

        rv_countries.layoutManager = gridLayoutManager
        rv_countries.setHasFixedSize(true)


        viewModel.countryDetails.observe(this, Observer {
            countryAdapter = CustomAdapter(this, it)
            countryAdapter.notifyDataSetChanged()
            rv_countries.adapter = countryAdapter
        })

        viewModel.networkState.observe(this, Observer {
            country_loader.visibility =
                if (viewModel.listisEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility =
                if (viewModel.listisEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })

        search_countries.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                countryAdapter.getFilter().filter(s)
                countryAdapter.notifyDataSetChanged()
            }

        })


    }

    private fun getViewModel(): CountriesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CountriesViewModel(countryDetailsRepository) as T
            }
        })[CountriesViewModel::class.java]

    }

}