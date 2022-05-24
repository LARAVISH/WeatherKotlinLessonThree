package com.githab.laravish.weatherkotlinlessonthree.viewmodel

import com.githab.laravish.weatherkotlinlessonthree.model.Weather

sealed class AppState {
    data class Loading(val load: String) : AppState()
    data class Success(val weather: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
}
