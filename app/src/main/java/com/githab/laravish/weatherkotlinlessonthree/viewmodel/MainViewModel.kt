package com.githab.laravish.weatherkotlinlessonthree.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.githab.laravish.weatherkotlinlessonthree.model.RepositoryImplement
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
) : ViewModel() {

    private val repository: RepositoryImplement by lazy { RepositoryImplement() }

    fun getLiveData() = liveData

    fun getWeatherFromLocalServerRusCities() = getWeather(true)
    fun getWeatherFromLocalServerWorldCities() = getWeather(false)

    private fun getWeather(isRus: Boolean) = with(repository) {
        liveData.postValue(AppState.Loading(" "))
        Thread {
            sleep(2000)
            setRandom(isRus)
        }.start()
    }

    private fun RepositoryImplement.setRandom(isRus: Boolean)  = with(liveData){
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