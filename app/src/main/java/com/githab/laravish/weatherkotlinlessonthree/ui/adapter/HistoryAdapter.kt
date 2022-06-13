package com.githab.laravish.weatherkotlinlessonthree.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.githab.laravish.weatherkotlinlessonthree.R
import com.githab.laravish.weatherkotlinlessonthree.data.Weather
import com.githab.laravish.weatherkotlinlessonthree.databinding.FragmentHistoryWeatherRecyclerViewItemBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {

    private var weatherList: List<Weather> = listOf()
    fun setData(data: List<Weather>) {
        this.weatherList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HistoryHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_history_weather_recycler_view_item, parent, false)
    )

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    override fun getItemCount() = weatherList.size

    inner class HistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) =
            with(FragmentHistoryWeatherRecyclerViewItemBinding.bind(itemView)) {
                cityName.text = weather.city.name
                temperature.text = "${weather.temperature}"
                feelsLike.text = "${weather.feelsLike}"
                icon.loadSvg("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
            }

        private fun ImageView.loadSvg(url: String) {
            val imageLoader = ImageLoader.Builder(context)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()
            val request = ImageRequest.Builder(this.context)
                .crossfade(true)
                .crossfade(500)
                .data(url)
                .target(this)
                .build()
            imageLoader.enqueue(request)
        }
    }
}