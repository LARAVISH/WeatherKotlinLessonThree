package com.githab.laravish.weatherkotlinlessonthree.model

class RepositoryImplement : Repository {
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}