package com.jpmc.weather.api

import retrofit2.Response

/**
 * Interface to the backend API.
 */
interface WeatherApi {
    /**
     * Fetches weather data matching the [searchCity].
     *
     * Operates on a background thread.
     */
    suspend fun fetchWeather(searchCity: String): Response<WeatherResponse>
}