package com.utrobin.luna

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric


/**
 * Created by ivan on 31.10.2017.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.create()
        configureCrashReporting()
    }

    private fun configureCrashReporting() {
        val crashlyticsCore = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()
        Fabric.with(this, Crashlytics.Builder().core(crashlyticsCore).build())
    }


    companion object {
        lateinit var component: AppComponent
            private set
    }
}