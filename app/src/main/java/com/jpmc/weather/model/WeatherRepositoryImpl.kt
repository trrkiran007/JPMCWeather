package com.jpmc.weather.model

import com.jpmc.weather.api.CityWeatherInfo
import com.jpmc.weather.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherApi: WeatherApi): WeatherRepository {

    private val LOG_TAG = WeatherRepositoryImpl::class.java.simpleName

    suspend fun fetchWeather(cityName: String): CityWeatherInfo {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApi.fetchWeather(cityName)
                if (response.isSuccessful) {
                    val res = response.body()
                    val cityWeatherInfo = CityWeatherInfo(
                        cityName = res?.name,
                        currTemp = res?.main?.temp,
                        daysHighTemp = res?.main?.temp_max,
                        daysLowTemp = res?.main?.temp_min,
                        iconUrl = "https://openweathermap.org/img/w/${res?.weather?.firstOrNull()?.icon}.png",
                        errorMessage = null)
                    cityWeatherInfo
                } else {
                    //throw Exception("Network request failed")
                    val cityWeatherInfo = CityWeatherInfo(
                        cityName = null,
                        currTemp = null,
                        daysHighTemp = null,
                        daysLowTemp = null,
                        iconUrl = null,
                        errorMessage = response.body()?.message ?: "Error fetching weather data for $cityName"
                    )
                    cityWeatherInfo
                }
            } catch (e: Exception) {
                //Log.i(LOG_TAG, e.message)
                throw Exception("Exception whie retrieving response")
            }
        }
    }

    suspend fun fetchWeather(lat: Double, lon: Double): CityWeatherInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApi.fetchWeather(lat, lon)
                if (response.isSuccessful) {
                    val res = response.body()
                    val cityWeatherInfo = CityWeatherInfo(
                        cityName = res?.name,
                        currTemp = res?.main?.temp,
                        daysHighTemp = res?.main?.temp_max,
                        daysLowTemp = res?.main?.temp_min,
                        iconUrl = "https://openweathermap.org/img/w/${res?.weather?.firstOrNull()?.icon}.png",
                        errorMessage = null)
                    cityWeatherInfo
                } else {
                    //throw Exception("Network request failed")
                    val cityWeatherInfo = CityWeatherInfo(
                        cityName = null,
                        currTemp = null,
                        daysHighTemp = null,
                        daysLowTemp = null,
                        iconUrl = null,
                        errorMessage = response.body()?.message ?: "Error fetching weather data."
                    )
                    cityWeatherInfo
                }
            } catch (e: Exception) {
                throw Exception("Network request failed")
            }
        }
    }

}