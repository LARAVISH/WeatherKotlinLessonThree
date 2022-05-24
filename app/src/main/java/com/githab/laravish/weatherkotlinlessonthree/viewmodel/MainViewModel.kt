package com.githab.laravish.weatherkotlinlessonthree.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.githab.laravish.weatherkotlinlessonthree.model.RepositoryImplement
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: RepositoryImplement = RepositoryImplement()
) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherFromLocalServerRusCities() = getWeather(true)
    fun getWeatherFromLocalServerWorldCities() = getWeather(false)

    private fun getWeather(isRus: Boolean) {
        AppState.Loading("Загрузка")
        Thread {
            sleep(2000)
            liveData.postValue(
                AppState.Success(
                    if (isRus) {
                        repository.getWeatherFromLocalStorageRus()
                    } else {
                        repository.getWeatherFromLocalStorageWorld()
                    }
                )
            )
        }.start()
    }
}