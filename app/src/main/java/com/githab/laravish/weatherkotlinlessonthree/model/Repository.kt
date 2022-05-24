package com.githab.laravish.weatherkotlinlessonthree.model

interface Repository {
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}