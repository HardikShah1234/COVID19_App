package com.livedata.covid19.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.livedata.covid19.R
import com.livedata.covid19.data.api.ApiClient
import com.livedata.covid19.data.api.ApiService
import com.livedata.covid19.data.repository.CoronaDetailsRepository
import com.livedata.covid19.data.repository.NetworkState
import com.livedata.covid19.models.MainActivityViewModel
import com.livedata.covid19.vo.CoronaResponse
import kotlinx.android.synthetic.main.activity_main.*
import org.eazegraph.lib.models.BarModel
import org.eazegraph.lib.models.PieModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var coronaRepository: CoronaDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar1))
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        val apiService: ApiService = ApiClient.getClient()
        coronaRepository =
            CoronaDetailsRepository(
                apiService
            )

        viewModel = getViewModel(1, 1)
        viewModel.coronaDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            loader.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_share -> {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Cases - " + viewModel.coronaDetails.value?.cases.toString() + System.lineSeparator() + "Recovered - " + viewModel.coronaDetails.value?.recovered.toString() + System.lineSeparator() + "Critical - " + viewModel.coronaDetails.value?.critical.toString() + System.lineSeparator() + "Active - " + viewModel.coronaDetails.value?.active.toString() + System.lineSeparator() + "Today Cases - " + viewModel.coronaDetails.value?.todayCases.toString() + System.lineSeparator() + "Total Deaths - " + viewModel.coronaDetails.value?.deaths.toString() + System.lineSeparator() + "Today Deaths - " + viewModel.coronaDetails.value?.todayDeaths.toString() + System.lineSeparator() + "Affected Countries - " + viewModel.coronaDetails.value?.affectedCountries.toString()
                )
                this.startActivity(Intent.createChooser(shareIntent, getString(R.string.Send)))
            }
            R.id.menu_settings -> Toast.makeText(applicationContext, "Settings", Toast.LENGTH_SHORT)
                .show()
            R.id.menu_exit -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Range")
    fun bindUI(it: CoronaResponse) {
        tv_cases.text = it.cases.toString()
        tv_recovered.text = it.recovered.toString()
        tv_active.text = it.active.toString()
        tv_critical.text = it.critical.toString()
        tv_today_cases.text = it.todayCases.toString()
        tv_total_deaths.text = it.deaths.toString()
        tv_affected_countries.text = it.affectedCountries.toString()
        tv_today_deaths.text = it.todayDeaths.toString()
        pie_chart.addPieSlice(
            PieModel(
                "Cases",
                Integer.parseInt(tv_cases.text.toString()).toFloat(), Color.parseColor("#FFA726")
            )
        )
        pie_chart.addPieSlice(
            PieModel(
                "Recovered",
                Integer.parseInt(tv_recovered.text.toString()).toFloat(),
                Color.parseColor("#66BB6A")
            )
        )
        pie_chart.addPieSlice(
            PieModel(
                "Deaths",
                Integer.parseInt(tv_total_deaths.text.toString()).toFloat(),
                Color.parseColor("#EF5350")
            )
        )
        pie_chart.addPieSlice(
            PieModel(
                "Active",
                Integer.parseInt(tv_active.text.toString()).toFloat(), Color.parseColor("#29B6F6")
            )
        )

        pie_chart.startAnimation()
        bar_chart.addBar(
            BarModel(
                "Cases",
                Integer.parseInt(tv_cases.text.toString()).toFloat(),
                Color.parseColor("#FFA726")
            )
        )
        bar_chart.addBar(
            BarModel(
                "Recovered",
                Integer.parseInt(tv_recovered.text.toString()).toFloat(),
                Color.parseColor("#66BB6A")
            )
        )
        bar_chart.addBar(
            BarModel(
                "Deaths",
                Integer.parseInt(tv_total_deaths.text.toString()).toFloat(),
                Color.parseColor("#EF5350")
            )
        )
        bar_chart.addBar(
            BarModel(
                "Active",
                Integer.parseInt(tv_active.text.toString()).toFloat(),
                Color.parseColor("#29B6F6")
            )
        )
        bar_chart.startAnimation()

    }

    private fun getViewModel(cases: Int, active: Int): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(
                    coronaRepository,
                    cases,
                    active
                ) as T
            }

        })[MainActivityViewModel::class.java]
    }

    fun trackCountries(view: View) {
        Intent(this, Countries::class.java).also { view.context.startActivity(it) }
    }
}