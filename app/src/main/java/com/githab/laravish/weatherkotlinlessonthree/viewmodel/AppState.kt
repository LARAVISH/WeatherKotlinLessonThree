package com.githab.laravish.weatherkotlinlessonthree.viewmodel

import com.githab.laravish.weatherkotlinlessonthree.data.Weather

sealed class AppState {
    object  Loading : AppState()
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
}
