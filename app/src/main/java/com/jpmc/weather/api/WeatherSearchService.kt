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

    //https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid={API key}
    @GET("weather")
    suspend fun fetchWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = "31e1a3ea7d1d4574fa14b4568de75447",
        @Query("units") units: String = "imperial"): Response<WeatherResponse>
}

/**
 * Response object.
 */
data class WeatherResponse(
    val main: MainInfo?,
    val weather: List<WeatherInfo>?,
    val message: String?,//when searched city not found - and code "404"
    val cod: String,
    val name: String
)

/**
 * Main weather data, we are interested in.
 */
data class MainInfo(
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

/**
 * The image url comes from this.
 */
data class WeatherInfo(
    val icon: String
)

/**
 * This is the Canonical model of the weather data that can be displayed on the UI.
 * I have included errorMessage to be shown in case the city data is not found, instead of keeping it blank
 * screen.
 */
data class CityWeatherInfo(
    val cityName: String?,
    val currTemp: Double?,
    val daysHighTemp: Double?,
    val daysLowTemp: Double?,
    val iconUrl: String?,
    val errorMessage: String?
)