package com.jpmc.weather

import android.app.Application
import com.jpmc.weather.dagger.AppComponent
import com.jpmc.weather.dagger.DaggerAppComponent

/**
 * The main Application class.
 */
class App : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}