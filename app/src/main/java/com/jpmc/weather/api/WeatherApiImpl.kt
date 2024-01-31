package com.jpmc.weather.api

import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Implementation of Weather api.
 * This has couple versions for supporting city based search and the other
 * for the lat, long based search.
 */
class WeatherApiImpl @Inject constructor(private val retrofit: Retrofit): WeatherApi {

    /**
     * Fetches weather data matching the [searchCity].
     *
     * Operates on a background thread.
     */
    override suspend fun fetchWeather(
        searchCity: String,
    ): Response<WeatherResponse> {
        val service = retrofit.create(WeatherSearchService::class.java)
        return service.fetchWeather(searchCity)
    }

    /**
     * Fetches weather data based on user's location
     */
    override suspend fun fetchWeather(lat: Double, long: Double): Response<WeatherResponse> {
        val service = retrofit.create(WeatherSearchService::class.java)
        return service.fetchWeather(lat, long)
    }


}