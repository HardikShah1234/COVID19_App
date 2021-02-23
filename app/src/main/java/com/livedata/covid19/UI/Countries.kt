package com.livedata.covid19.UI

import android.content.Intent
import android.os.Bundle
import android.speech.tts.UtteranceProgressListener
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewParent
import android.widget.AdapterView
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.activity_countries.view.*
import kotlinx.android.synthetic.main.flag_list.*
import kotlinx.android.synthetic.main.flag_list.view.*
import java.text.FieldPosition

@Suppress("UNCHECKED_CAST")
class Countries : AppCompatActivity() {


    private lateinit var viewModel: CountriesViewModel
    private lateinit var countryDetailsRepository: CountryDetailsRepository
    lateinit var countryAdapter: CustomAdapter
    public var countryList = arrayListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)

        val apiService: ApiService = ApiClient.getClient()
        countryDetailsRepository = CountryDetailsRepository(apiService)

        viewModel = getViewModel()
//        val gridLayoutManager = GridLayoutManager(this, 3)
//
//        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                val viewType = countryAdapter.getItemViewType(position)
//                if (viewType == countryAdapter.VIEW_TYPE) return 1
//                else return 3
//            }
//        }

        rv_countries.layoutManager = LinearLayoutManager(this)
        rv_countries.setHasFixedSize(true)



        viewModel.countryDetails.observe(this, Observer {
//            val itemOnClick : View.OnClickListener = View.OnClickListener {
//                val mRecyclerView  = RecyclerView(this)
//                val itemPosition = mRecyclerView.getChildLayoutPosition(it)
//                val item : String = countryAdapter.country.get(itemPosition).toString()
//
//                    if(it.cv_flag.getVisibility() == View.VISIBLE){
//                        mRecyclerView.findContainingItemView(it)
//                        mRecyclerView.findViewHolderForAdapterPosition(itemPosition)
//                        mRecyclerView.get(itemPosition).display
//                        mRecyclerView.setVisibility(View.GONE)
//                        it.cv_flag.setVisibility(View.GONE)
//                        it.cv_country_data.visibility = View.VISIBLE
//                    } else {
//                        mRecyclerView.setVisibility(View.VISIBLE)
//                        it.cv_flag.setVisibility(View.VISIBLE)
//                        it.cv_country_data.visibility = View.GONE
//                    }
//
//            }
            countryAdapter = CustomAdapter(this, it)
            rv_countries.adapter = countryAdapter
            countryAdapter.notifyDataSetChanged()
        })

        viewModel.networkState.observe(this, Observer {
            country_loader.visibility =
                if (viewModel.listisEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility =
                if (viewModel.listisEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })


//        rv_countries.setOnClickListener {
//            val mRecyclerView = RecyclerView(this)
//            val itemPosition = mRecyclerView.getChildLayoutPosition(it)
//            val intent =
//                Intent(applicationContext, CountryWiseDataActivity::class.java).putExtra("position", itemPosition)
//            startActivity(intent)
//        }
        search_countries.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countryAdapter.filter.filter(newText)
                return false
            }
        })

//        search_countries.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                countryAdapter.getFilter().filter(s.toString())
//            }
//
//        })


    }


    private fun getViewModel(): CountriesViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CountriesViewModel(countryDetailsRepository) as T
            }
        })[CountriesViewModel::class.java]

    }

}