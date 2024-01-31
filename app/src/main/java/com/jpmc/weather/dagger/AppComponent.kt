package com.jpmc.weather.dagger

import com.jpmc.weather.view.MainActivity
import com.jpmc.weather.viewmodel.WeatherViewModel
import dagger.Component

/**
 * Dagger component for the application.
 */
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(viewModel: WeatherViewModel)
}