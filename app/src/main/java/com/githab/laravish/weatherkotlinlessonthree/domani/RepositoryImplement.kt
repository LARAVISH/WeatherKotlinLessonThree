package com.githab.laravish.weatherkotlinlessonthree.domani

import android.os.Build
import androidx.annotation.RequiresApi
import com.githab.laravish.weatherkotlinlessonthree.data.City
import com.githab.laravish.weatherkotlinlessonthree.data.Weather
import com.githab.laravish.weatherkotlinlessonthree.data.getRussianCities
import com.githab.laravish.weatherkotlinlessonthree.data.getWorldCities
import com.githab.laravish.weatherkotlinlessonthree.rest.WeatherRepo
import com.githab.laravish.weatherkotlinlessonthree.room.HistoryDatabase
import com.githab.laravish.weatherkotlinlessonthree.room.HistoryWeatherEntities

class RepositoryImplement(private val db: HistoryDatabase) : Repository {

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

    override fun saveEntities(weather: Weather) {
        return db.historyWeatherDao().insert(converterFromWeatherToHistoryWeather(weather))
    }

    override fun getAllHistoryWeather(): List<Weather> {
        return converterFromHistoryWeatherToWeather(db.historyWeatherDao().getAllHistoryWeather())
    }

    private fun converterFromHistoryWeatherToWeather(entitiesLists: List<HistoryWeatherEntities>): List<Weather> {
        return entitiesLists.map {
            Weather(City(it.city, 0.0, 0.0),
                it.temperature,
                it.feelsLike,
                it.icon)
        }
    }

    private fun converterFromWeatherToHistoryWeather(weather: Weather): HistoryWeatherEntities {
        return HistoryWeatherEntities(0,
            weather.city.name,
            weather.temperature,
            weather.feelsLike,
            weather.icon)
    }
}