package com.utrobin.luna

import com.utrobin.luna.network.NetworkModule
import com.utrobin.luna.ui.presenter.*
import dagger.Component


/**
 * Created by ivan on 31.10.2017.
 */

@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun injectFeedPresenter(presenter: FeedPresenter)
    fun injectMasterPresenter(presenter: MasterPresenter)
    fun injectCommonSignUpPresenter(presenter: CommonSignUpPresenter)
    fun injectMasterSignUpPresenter(presenter: MasterSignUpPresenter)
    fun injectUserSignUpPresenter(presenter: ClientSignUpPresenter)
}