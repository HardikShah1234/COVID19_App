package com.livedata.covid19.data.repository

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livedata.covid19.R
import com.livedata.covid19.UI.CountryWiseDataActivity
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import kotlinx.android.synthetic.main.flag_list.view.*

class CustomAdapter(public val context: Context, private val countriesResponse: CountriesResponse) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>(), Filterable {

    var country = ArrayList<CountriesResponseItem>()
    var list_country: ArrayList<CountriesResponseItem>
    internal var mFilter: NewFilter

    override fun getFilter(): Filter {
        return mFilter
    }

    init {
        list_country = getCountries()
        country = ArrayList()
        country.addAll(list_country)
        mFilter = NewFilter(this@CustomAdapter)
    }

    private fun getCountries(): ArrayList<CountriesResponseItem> {
        val list_country = arrayListOf<CountriesResponseItem>()
        for (list1 in countriesResponse)
            list_country.add(list1)
        return list_country
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView
        var cv_tv_country_name: TextView

        init {
            image = itemView.cv_iv_country_flag
            cv_tv_country_name = itemView.cv_tv_country_name
        }
    }


    val VIEW_TYPE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.flag_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return country.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(country[position].countryInfo.flag).into(holder.image)
        holder.itemView.setOnClickListener {
            val intent =
                Intent(context, CountryWiseDataActivity::class.java).putExtra("position", position)
            context.startActivity(intent)
        }
        holder.cv_tv_country_name.text = country[position].country
    }

    inner class NewFilter(var customAdapter: CustomAdapter) : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            country.clear()
            val results = FilterResults()
            if (constraint.isNullOrEmpty()) {
                country.addAll(list_country)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim() { it <= ' ' }
                for (list_country in 0..list_country.size) {
                    if (countriesResponse[list_country].country.toLowerCase()
                            .startsWith(filterPattern)
                    ) {
                        country.add(countriesResponse[list_country])
                    }
                }
            }

            results.values = country
            results.count = country.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            this.customAdapter.notifyDataSetChanged()
        }

    }
}