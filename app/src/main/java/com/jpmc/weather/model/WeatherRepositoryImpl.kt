package com.jpmc.weather.model

import com.jpmc.weather.api.CityWeatherInfo
import com.jpmc.weather.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherApi: WeatherApi): WeatherRepository {
    suspend fun fetchWeather(cityName: String): CityWeatherInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApi.fetchWeather(cityName)
                if (response.isSuccessful) {
                    val res = response.body()
                    val cityWeatherInfo = CityWeatherInfo(
                        cityName = cityName,
                        currTemp = res?.main?.temp,
                        daysHighTemp = res?.main?.tempMax,
                        daysLowTemp = res?.main?.tempMin,
                        iconUrl = "https://openweathermap.org/img/w/${res?.weather?.firstOrNull()?.icon}.png")
                    cityWeatherInfo
                } else {
                    throw Exception("Network request failed")
                }
            } catch (e: Exception) {
                throw Exception("Network request failed")
            }
        }
    }

}