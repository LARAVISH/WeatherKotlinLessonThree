package com.githab.laravish.weatherkotlinlessonthree.rest

import com.githab.laravish.weatherkotlinlessonthree.di.BASE_URL_MAIN_PART
import com.githab.laravish.weatherkotlinlessonthree.di.BASE_VERSION
import com.githab.laravish.weatherkotlinlessonthree.di.YANDEX_API
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApiUtils {
    private const val baseUrlMainPart = BASE_URL_MAIN_PART
    private const val baseVersions = BASE_VERSION
    const val baseUrl = "$baseUrlMainPart$baseVersions"

    fun getOkHTTPBuilderWithHeaders(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(1000, TimeUnit.SECONDS)
        okHttpClient.readTimeout(1000, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(1000, TimeUnit.SECONDS)
        okHttpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header(YANDEX_API, "e7848cdd-d064-424d-becc-9ba74966172a")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        return okHttpClient.build()
    }
}




