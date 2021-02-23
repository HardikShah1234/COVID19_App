package com.livedata.covid19.data.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livedata.covid19.R
import com.livedata.covid19.UI.Countries
import com.livedata.covid19.UI.CountryWiseDataActivity
import com.livedata.covid19.vo.CountriesResponse
import com.livedata.covid19.vo.CountriesResponseItem
import kotlinx.android.synthetic.main.flag_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapter(public val context: Context, private var countriesResponse: CountriesResponse) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>(), Filterable {

    var countryFilterList = CountriesResponse()

    init {
        countryFilterList = countriesResponse
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView
        var cv_tv_country_name: TextView

        init {
            //image = itemView.cv_iv_country_flag
            image = itemView.findViewById(R.id.cv_iv_country_flag)
            cv_tv_country_name = itemView.cv_tv_country_name

        }
    }

    val VIEW_TYPE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.flag_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return countryFilterList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(countryFilterList[position].countryInfo.flag).into(holder.image)
        holder.itemView.setOnClickListener {
            val intent =
                Intent(context, CountryWiseDataActivity::class.java).putExtra("position", position)
            context.startActivity(intent)
        }
        holder.cv_tv_country_name.text = countryFilterList[position].country
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    countryFilterList = countriesResponse
                } else {
                    val resultList = CountriesResponse()
                    for (row in countriesResponse) {
                        if (row.country.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    countryFilterList = resultList
                    countriesResponse = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as CountriesResponse
                notifyDataSetChanged()
            }

        }
    }
}