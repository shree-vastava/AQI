package com.acous.airqualityindex.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acous.airqualityindex.R
import com.acous.airqualityindex.data.CityAqi
import com.acous.airqualityindex.util.CityClickListener
import com.github.marlonlom.utilities.timeago.TimeAgo
import kotlinx.android.synthetic.main.layout_city.view.*


class MainAdapter(private val cityClickListener: CityClickListener) :

    RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    var citiesList = ArrayList<CityAqi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.layout_city, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cityModel = citiesList[position]
        holder.itemView.tvCityName.apply {
            transitionName = "transition"
            text = cityModel.city
        }
        holder.itemView.tvAqiValue.text = String.format("%.2f", cityModel.aqi)
        holder.itemView.tv_last_updated.text = "Last updated ${TimeAgo.using(cityModel.lastSyncTime)}"
        cityModel.colorValue?.let { holder.itemView.view_indicator.setBackgroundColor(holder.itemView.context.getColor(
            cityModel.colorValue!!
        )) }

        holder.itemView.setOnClickListener {
            cityClickListener.onCityClicked(cityModel, holder.itemView.tvCityName)
        }
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun updateList(newCitiesList: ArrayList<CityAqi>){
        citiesList = newCitiesList
        notifyDataSetChanged()
    }
}