package com.utrobin.luna

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import okhttp3.OkHttpClient


/**
 * Created by ivan on 31.10.2017.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.create()
        configureApolloClient()
        configureCrashReporting()
    }

    private fun configureApolloClient() {
        val okHttpClient = OkHttpClient.Builder().build()
        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();
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

        lateinit var apolloClient: ApolloClient
            private set

        private const val BASE_URL = "https://utrobin.com/api/graphql"
    }
}