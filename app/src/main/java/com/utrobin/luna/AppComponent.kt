package com.utrobin.luna

import com.utrobin.luna.network.NetworkModule
import com.utrobin.luna.ui.presenter.FeedPresenter
import dagger.Component


/**
 * Created by ivan on 31.10.2017.
 */

@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun injectsFeedPresenter(presenter: FeedPresenter)
}