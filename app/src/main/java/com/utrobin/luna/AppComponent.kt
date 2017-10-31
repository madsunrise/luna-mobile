package com.utrobin.luna

import com.utrobin.luna.activity.MainActivity
import com.utrobin.luna.network.NetworkModule
import dagger.Component


/**
 * Created by ivan on 31.10.2017.
 */

@Component(modules = arrayOf(NetworkModule::class))
interface AppComponent {
    fun injectsMainActivity(mainActivity: MainActivity)
}