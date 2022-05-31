package com.githab.laravish.weatherkotlinlessonthree.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.githab.laravish.weatherkotlinlessonthree.model.Repository

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val localLiveData = MutableLiveData<AppState>()
    val liveData: LiveData<AppState>
        get() = localLiveData

    fun getWeatherFromLocalServerRusCities() = getWeather(true)
    fun getWeatherFromLocalServerWorldCities() = getWeather(false)

    private fun getWeather(isRus: Boolean) {
        localLiveData.value = AppState.Loading
        Thread {
            Thread.sleep(1000)
            localLiveData.postValue(
                if (isRus) {
                    AppState.Success(repository.getWeatherFromLocalStorageRus())
                } else {
                    AppState.Success(repository.getWeatherFromLocalStorageWorld())
                }
            )
        }.start()
    }
}