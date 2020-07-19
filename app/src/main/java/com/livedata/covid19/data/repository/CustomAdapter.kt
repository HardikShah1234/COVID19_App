package com.livedata.covid19.data.repository

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livedata.covid19.R
import com.livedata.covid19.UI.CountryWiseDataActivity
import com.livedata.covid19.vo.CountriesResponseItem
import kotlinx.android.synthetic.main.flag_list.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class CustomAdapter(public val context: Context) :
    PagedListAdapter<CountriesResponseItem, RecyclerView.ViewHolder>(CountryDiffCallback()) {

    val VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    private var networkState : NetworkState? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view : View

        if (viewType == VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.flag_list, parent, true)
            return FlagItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item,parent,true)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE){
            (holder as FlagItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow():Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            VIEW_TYPE
        }
    }

    class CountryDiffCallback : DiffUtil.ItemCallback<CountriesResponseItem>(){
        override fun areItemsTheSame(
            oldItem: CountriesResponseItem,
            newItem: CountriesResponseItem
        ): Boolean {
            return oldItem.country == newItem.country
        }

        override fun areContentsTheSame(
            oldItem: CountriesResponseItem,
            newItem: CountriesResponseItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    class FlagItemViewHolder (view: View) : RecyclerView.ViewHolder(view){

        fun bind(countriesResponseItem: CountriesResponseItem?, context: Context){
            itemView.cv_tv_country_name.text = countriesResponseItem?.country

            Glide.with(itemView.context).load(countriesResponseItem?.flag).into(itemView.cv_iv_country_flag)
            itemView.setOnClickListener {
                val intent = Intent(context, CountryWiseDataActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view){
        fun bind(networkState: NetworkState?){
            if (networkState != null && networkState == NetworkState.LOADING){
                itemView.arc_loader_network.visibility = View.VISIBLE
            } else {
                itemView.arc_loader_network.visibility = View.GONE
            }
            if (networkState != null && networkState == NetworkState.ERROR){
                itemView.tv_error_message_item.visibility = View.VISIBLE
                itemView.tv_error_message_item.text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.ENDLIST){
                itemView.tv_error_message_item.visibility = View.VISIBLE
                itemView.tv_error_message_item.text = networkState.msg
            } else {
                itemView.tv_error_message_item.visibility = View.GONE
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}