package com.githab.laravish.weatherkotlinlessonthree.room

import androidx.room.*

@Dao
interface HistoryWeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entities: HistoryWeatherEntities)

    @Query("SELECT * FROM HistoryWeatherEntities")
    fun getAllHistoryWeather(): List<HistoryWeatherEntities>

    @Delete
    fun delete(entities: HistoryWeatherEntities)

    @Update
    fun update(entities: HistoryWeatherEntities)
}
