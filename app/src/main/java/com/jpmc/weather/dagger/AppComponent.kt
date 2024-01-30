package com.jpmc.weather.dagger

import com.jpmc.weather.view.MainActivity
import com.jpmc.weather.viewmodel.WeatherViewModel
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(viewModel: WeatherViewModel)
}