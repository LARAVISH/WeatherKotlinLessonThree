package com.githab.laravish.weatherkotlinlessonthree.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.githab.laravish.weatherkotlinlessonthree.R
import com.githab.laravish.weatherkotlinlessonthree.model.Weather

class MainFragmentAdapter(val listener: MyOnClickListener) :
    RecyclerView.Adapter<MainFragmentAdapter.MyViewHolder>() {

    private var weather: List<Weather> = listOf()

    fun setData(data: List<Weather>) {
        this.weather = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_details_citi_view, parent, false)
    )


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(this.weather[position])
    }

    override fun getItemCount() = this.weather.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) = with(itemView) {
            findViewById<TextView>(R.id.titleCity).text = weather.city.name
            setOnClickListener {
                listener.onClick(weather)
            }
        }
    }
}