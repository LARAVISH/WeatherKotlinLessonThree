package com.githab.laravish.weatherkotlinlessonthree.model.rest

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApiUtils {
    private const val baseUrlMainPart = "https://api.weather.yandex.ru/"
    private const val baseVersions = "v2/"
    const val baseUrl = "$baseUrlMainPart$baseVersions"

    fun getOkHTTPBuilderWithHeaders(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(1000, TimeUnit.SECONDS)
        okHttpClient.readTimeout(1000, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(1000, TimeUnit.SECONDS)
        okHttpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("X-Yandex-API-Key", "e7848cdd-d064-424d-becc-9ba74966172a")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        return okHttpClient.build()
    }
}




