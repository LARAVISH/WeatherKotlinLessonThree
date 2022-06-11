package com.githab.laravish.weatherkotlinlessonthree.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githab.laravish.weatherkotlinlessonthree.domani.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class DetailsViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData: MutableLiveData<AppState> = MutableLiveData()
    val weatherLiveData: LiveData<AppState> get() = localLiveData

    fun loadData(lat: Double, lon: Double) {
        localLiveData.value = AppState.Loading
        viewModelScope.launch(Dispatchers.Main) {
            val task = async(Dispatchers.IO) {
                repository.getWeatherFromServer(lat, lon)
            }
            val data = task.await()
            if (isActive) {
                localLiveData.value = AppState.Success(listOf(data))
            }
        }
    }
}


