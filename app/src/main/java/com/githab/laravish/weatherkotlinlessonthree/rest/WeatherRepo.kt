package com.githab.laravish.weatherkotlinlessonthree.rest

import com.githab.laravish.weatherkotlinlessonthree.domani.WeatherAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRepo {
    val api: WeatherAPI by lazy {
        val adapter = Retrofit.Builder().baseUrl(ApiUtils.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(ApiUtils.getOkHTTPBuilderWithHeaders())
            .build()
        adapter.create(WeatherAPI::class.java)
    }
}