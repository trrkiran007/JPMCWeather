package com.jpmc.weather.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Weather search service, used for searching weather info for any US city with [searchCity],
 * apiKey - openweathermap.org apiKey and units - unit system (default imperial).
 *
 */
interface WeatherSearchService {
    @GET("weather")
    suspend fun fetchWeather(
        @Query("q") searchCity: String,
        @Query("appid") apiKey: String = "31e1a3ea7d1d4574fa14b4568de75447",//api-Key
        @Query("units") units: String = "imperial"): Response<WeatherResponse>//setting default units as imperial, i.e., Fahrenheit for temperature.
}

data class WeatherResponse(
    val main: MainInfo?,
    val weather: List<WeatherInfo>?,
    val message: String?,//when searched city not found - and code "404"
    val cod: String?
)

data class MainInfo(
    val temp: Double,
    val tempMax: Double,
    val tempMin: Double
)

data class WeatherInfo(
    val icon: String
)

data class CityWeatherInfo(
    val cityName: String,
    val currTemp: Double?,
    val daysHighTemp: Double?,
    val daysLowTemp: Double?,
    val iconUrl: String
)