package com.utrobin.luna

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
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