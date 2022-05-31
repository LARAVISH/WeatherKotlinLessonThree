package com.githab.laravish.weatherkotlinlessonthree.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.githab.laravish.weatherkotlinlessonthree.model.Repository
import java.lang.Thread.sleep

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val localLiveData= MutableLiveData<AppState>( )
    val liveData : LiveData<AppState>
    get() = localLiveData

    fun getWeatherFromLocalServerRusCities() = getWeather(true)
    fun getWeatherFromLocalServerWorldCities() = getWeather(false)

    private fun getWeather(isRus: Boolean) = with(repository) {
        localLiveData.postValue(AppState.Loading(" "))
        Thread {
            sleep(2000)
            setRandom(isRus)
        }.start()
    }

    private fun Repository.setRandom(isRus: Boolean)  = with(localLiveData){
        val ren = (0..40).random()
        if (ren > 20) {
            postValue(
                AppState.Success(
                    if (isRus) {
                        getWeatherFromLocalStorageRus()
                    } else {
                        getWeatherFromLocalStorageWorld()
                    }
                )
            )
        } else {
            postValue(AppState.Error(IllegalArgumentException()))
        }
    }
}