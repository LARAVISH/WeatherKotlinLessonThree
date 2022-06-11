package com.githab.laravish.weatherkotlinlessonthree.domani

import android.os.Build
import androidx.annotation.RequiresApi
import com.githab.laravish.weatherkotlinlessonthree.data.Weather
import com.githab.laravish.weatherkotlinlessonthree.data.getRussianCities
import com.githab.laravish.weatherkotlinlessonthree.data.getWorldCities
import com.githab.laravish.weatherkotlinlessonthree.rest.WeatherRepo

class RepositoryImplement : Repository {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getWeatherFromServer(lat: Double, lon: Double): Weather {
        val dto = WeatherRepo.api.getWeather(lat, lon).execute().body()
        return Weather(
            temperature = dto?.fact?.temp ?: 0,
            feelsLike = dto?.fact?.feelsLike ?: 0,
            condition = dto?.fact?.condition ?: "cloudy ",
            icon = dto?.fact?.icon ?: "pictures"
        )
    }
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}