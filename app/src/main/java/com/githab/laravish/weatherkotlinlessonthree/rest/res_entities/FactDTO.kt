package com.githab.laravish.weatherkotlinlessonthree.rest.res_entities

import com.google.gson.annotations.SerializedName


data class FactDTO(
    val temp: Int?,
    @SerializedName("feels_like") val feelsLike: Int?,
    @SerializedName("icon")
    val icon: String?,
    val condition: String?,
)

