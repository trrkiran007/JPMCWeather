package com.jpmc.weather.api

import retrofit2.Response

/**
 * Interface to the backend API. This has couple versions for supporting city based search and the other
 * for the lat, long based search.
 */
interface WeatherApi {
    /**
     * Fetches weather data matching the [searchCity].
     *
     * Operates on a background thread.
     */
    suspend fun fetchWeather(searchCity: String): Response<WeatherResponse>

    /**
     * Fetches weather data based on user's location
     */
    suspend fun fetchWeather(lat: Double, long: Double): Response<WeatherResponse>
}