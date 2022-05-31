package com.githab.laravish.weatherkotlinlessonthree.model

interface Repository {
    fun getWeatherFromServer(lat: Double, lon: Double): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>

}