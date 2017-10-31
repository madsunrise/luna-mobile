package com.utrobin.luna

import android.app.Application

/**
 * Created by ivan on 31.10.2017.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.create()
    }

    companion object {
        lateinit var component: AppComponent
            private set
    }
}