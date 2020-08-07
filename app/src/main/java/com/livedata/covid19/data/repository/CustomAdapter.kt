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
import kotlinx.android.synthetic.main.flag_list.view.*

class CustomAdapter(public val context: Context, private val countriesResponse: CountriesResponse) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>(), Filterable {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView
        var cv_tv_country_name: TextView

        init {
            image = itemView.cv_iv_country_flag
            cv_tv_country_name = itemView.cv_tv_country_name

        }
    }

    val VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    var searchableList: ArrayList<CountriesResponse> = arrayListOf()
    val countries = ArrayList<CountriesResponse>()
    private var onNothingFound: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.flag_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return countriesResponse.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(countriesResponse[position].countryInfo.flag).into(holder.image)
        holder.itemView.setOnClickListener {
            val intent =
                Intent(context, CountryWiseDataActivity::class.java).putExtra("position", position)
            context.startActivity(intent)
        }
        holder.cv_tv_country_name.text = countriesResponse[position].country
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableList.clear()
                if (constraint.isNullOrBlank()) {
                    searchableList.addAll(countries)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                    for (item in 0..countries.size) {
                        if (countries[item].get(countries.size).country!!.toLowerCase()
                                .contains(filterPattern)
                        ) {
                            searchableList.add(countries[item])
                        }
                    }
                }
                return filterResults.also {
                    it.values = countries
                    it.count = countries.size
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (searchableList.isNullOrEmpty())
                    onNothingFound?.invoke()
                notifyDataSetChanged()
            }

        }
    }

    override fun getItemId(position: Int): Long {
        return countries[position].size.toLong()
    }
}