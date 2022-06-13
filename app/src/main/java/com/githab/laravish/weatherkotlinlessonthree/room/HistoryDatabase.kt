package com.githab.laravish.weatherkotlinlessonthree.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryWeatherEntities::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase(){
    abstract fun historyWeatherDao() : HistoryWeatherDao
}