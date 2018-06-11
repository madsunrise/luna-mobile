package com.utrobin.luna.utils

import android.util.Log
import com.crashlytics.android.Crashlytics

object LogUtils {
    @JvmStatic
    fun log(source: Class<out Any>, msg: String) {
        this.log(source, msg, Log.DEBUG)
    }

    @JvmStatic
    fun log(source: Class<out Any>, msg: String, logLevel: Int) {
        when (logLevel) {
            Log.VERBOSE -> Log.v(source.simpleName, msg)
            Log.DEBUG -> Log.d(source.simpleName, msg)
            Log.INFO -> Log.i(source.simpleName, msg)
            Log.WARN -> Log.w(source.simpleName, msg)
            Log.ERROR -> Log.e(source.simpleName, msg)
            Log.ASSERT -> Log.wtf(source.simpleName, msg)
        }
    }

    @JvmStatic
    fun logException(source: Class<out Any>, ex: Throwable) {
        this.logException(source, ex.message ?: "Exception in ${source.simpleName}", ex)
    }

    @JvmStatic
    fun logException(source: Class<out Any>, msg: String, ex: Throwable) {
        Log.e(source.simpleName, msg, ex)
        Crashlytics.logException(ex)
    }
}