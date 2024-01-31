package com.jpmc.weather.model

import com.jpmc.weather.api.MainInfo
import com.jpmc.weather.api.WeatherApi
import com.jpmc.weather.api.WeatherInfo
import com.jpmc.weather.api.WeatherResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class WeatherRepositoryImplTest {

    private lateinit var weatherApi: WeatherApi
    private lateinit var weatherRepository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        weatherApi = mock(WeatherApi::class.java)
        weatherRepository = WeatherRepositoryImpl(weatherApi)
    }

    @Test
    fun fetchWeather_success() = runBlocking {
        val cityName = "London"
        val weatherResponse = createMockWeatherResponse(cityName)
        `when`(weatherApi.fetchWeather(cityName)).thenReturn(Response.success(weatherResponse))
        val result = weatherRepository.fetchWeather(cityName)
        assertNotNull(result)
        assertEquals(cityName, result.cityName)
    }

    @Test
    fun fetchWeatherByLocation_success() = runBlocking {
        val lat = 51.5074
        val lon = -0.1278
        val weatherResponse = createMockWeatherResponse("London")
        `when`(weatherApi.fetchWeather(lat, lon)).thenReturn(Response.success(weatherResponse))
        val result = weatherRepository.fetchWeather(lat, lon)
        assertNotNull(result)
    }

    private fun createMockWeatherResponse(cityName: String): WeatherResponse {
        // Creating a mock WeatherResponse object for testing
        return WeatherResponse(
            name = cityName,
            main = MainInfo(temp = 20.0, temp_max = 25.0, temp_min = 15.0),
            weather = listOf(WeatherInfo(icon = "01d")),
            cod = "200",
            message = ""
        )
    }

}
