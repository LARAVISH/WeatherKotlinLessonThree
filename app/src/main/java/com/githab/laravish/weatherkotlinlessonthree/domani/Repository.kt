package com.githab.laravish.weatherkotlinlessonthree.domani

import com.githab.laravish.weatherkotlinlessonthree.data.Weather

interface Repository {
    fun getWeatherFromServer(lat: Double, lon: Double): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>

}