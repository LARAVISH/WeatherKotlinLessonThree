package com.githab.laravish.weatherkotlinlessonthree.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githab.laravish.weatherkotlinlessonthree.domani.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val localLiveData = MutableLiveData<AppState>()
    val liveData: LiveData<AppState>
        get() = localLiveData

    fun getWeatherFromLocalServerRusCities() = getWeather(true)
    fun getWeatherFromLocalServerWorldCities() = getWeather(false)

    private fun getWeather(isRus: Boolean) {
        localLiveData.value = AppState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            if (isActive) {
                localLiveData.postValue(
                    if (isRus) {
                        AppState.Success(repository.getWeatherFromLocalStorageRus())
                    } else {
                        AppState.Success(repository.getWeatherFromLocalStorageWorld())
                    }
                )
            }
        }
    }
}