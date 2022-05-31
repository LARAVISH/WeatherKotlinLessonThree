package com.githab.laravish.weatherkotlinlessonthree.viewmodel

import com.githab.laravish.weatherkotlinlessonthree.model.Weather

sealed class AppState {
    object  Loading : AppState()
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
}
