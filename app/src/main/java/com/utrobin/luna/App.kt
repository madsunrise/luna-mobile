package com.utrobin.luna

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import io.fabric.sdk.android.Fabric
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody


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
        val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor(TransformRequestInterceptor)
                .build()
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


    private val TransformRequestInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        // TODO move variables

        val originalResponse = chain.proceed(originalRequest)

        if (originalResponse.code() != 200) {
            return@Interceptor originalResponse // just forwarding
        }

        originalResponse.body()?.let { body ->
            val json = originalResponse.body()?.string()

            if (Gson().fromJson<ResponseCode>(json, ResponseCode::class.java).code != 200) {
                return@Interceptor originalResponse  // just forwarding
            }

            val model = Gson().fromJson<TransformedResponse>(json, TransformedResponse::class.java)
            val newJson = Gson().toJson(model)

            val contentType = body.contentType()
            val newBody = ResponseBody.create(contentType, newJson)
            originalResponse.newBuilder().body(newBody).build()
        } ?: throw NotImplementedError()
    }

    inner class ResponseCode {
        @SerializedName("code")
        var code: Int = 0
    }

    inner class TransformedResponse {
        @SerializedName(value = "data", alternate = arrayOf("body"))
        lateinit var data: JsonElement
    }


    companion object {
        lateinit var component: AppComponent
            private set

        lateinit var apolloClient: ApolloClient
            private set

        private const val BASE_URL = "https://utrobin.com/api/graphql"

        private val TAG = App::class.java.simpleName
    }
}