package com.jpmc.weather.api

import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

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


}