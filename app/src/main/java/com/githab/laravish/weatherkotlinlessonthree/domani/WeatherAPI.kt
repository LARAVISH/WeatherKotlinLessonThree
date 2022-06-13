package com.githab.laravish.weatherkotlinlessonthree.domani

import com.githab.laravish.weatherkotlinlessonthree.YANDEX_API_GET_INFORMERS
import com.githab.laravish.weatherkotlinlessonthree.rest.res_entities.WeatherDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {
    @GET(YANDEX_API_GET_INFORMERS)
    fun getWeather(@Query("lat") lat: Double, @Query("lon") lon: Double): Call<WeatherDTO>
}