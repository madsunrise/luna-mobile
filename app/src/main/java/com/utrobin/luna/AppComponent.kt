package com.utrobin.luna

import com.utrobin.luna.network.NetworkModule
import com.utrobin.luna.ui.presenter.AuthPresenter
import com.utrobin.luna.ui.presenter.FeedPresenter
import com.utrobin.luna.ui.presenter.MasterPresenter
import dagger.Component


/**
 * Created by ivan on 31.10.2017.
 */

@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun injectFeedPresenter(presenter: FeedPresenter)
    fun injectMasterPresenter(presenter: MasterPresenter)
    fun injectAuthPresenter(presenter: AuthPresenter)
}