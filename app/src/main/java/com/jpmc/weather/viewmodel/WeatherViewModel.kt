package com.jpmc.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpmc.weather.api.CityWeatherInfo
import com.jpmc.weather.model.WeatherRepositoryImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepositoryImpl) : ViewModel() {
    private val _weatherInfo = MutableLiveData<CityWeatherInfo>()
    val weatherInfo: LiveData<CityWeatherInfo>
        get() = _weatherInfo

    // ViewModel method to fetch weather data
    fun fetchWeather(cityName: String) {
        viewModelScope.launch {
            try {
                val cityWeatherInfo = weatherRepository.fetchWeather(cityName)
                _weatherInfo.value = cityWeatherInfo
            } catch (e: Exception) {
                // Handle the error, maybe update UI or show a Toast
                e.printStackTrace()
            }
        }
    }
}