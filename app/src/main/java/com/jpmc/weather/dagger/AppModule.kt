package com.jpmc.weather.dagger

import com.jpmc.weather.api.WeatherApi
import com.jpmc.weather.api.WeatherApiImpl
import com.jpmc.weather.model.WeatherRepository
import com.jpmc.weather.model.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * App Module, provides Retrofit and also Binds Weather API and Weather repository.
 */
@Module
abstract class AppModule {

    companion object {
        @Provides
        fun provideRetrofit(): Retrofit {
            val BASE_URL =
                "https://api.openweathermap.org/data/2.5/" //apiKey: String, units: String

            val okHttpClient = OkHttpClient.Builder().build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit
        }

    }

    @Binds
    abstract fun bindWeatherApi(
        weatherApiProvider: WeatherApiImpl
    ): WeatherApi

    @Binds
    abstract fun bindWeatherRepository(
        weatherRepository: WeatherRepositoryImpl
    ): WeatherRepository
}