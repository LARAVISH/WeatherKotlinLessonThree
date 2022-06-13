package com.githab.laravish.weatherkotlinlessonthree.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryWeatherEntities(
    @PrimaryKey(autoGenerate = true) val id: Long, val city: String, var temperature: Int,
    var feelsLike: Int,
    val icon: String,
)
