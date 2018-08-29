package com.utrobin.luna

import com.utrobin.luna.network.NetworkModule
import com.utrobin.luna.ui.presenter.*
import dagger.Component

@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun injectFeedPresenter(presenter: FeedPresenter)
    fun injectSalonPresenter(presenter: SalonPresenter)
    fun injectMasterPresenter(presenter: MasterPresenter)
    fun injectCommonSignUpPresenter(presenter: CommonSignUpPresenter)
    fun injectMasterSignUpPresenter(presenter: MasterSignUpPresenter)
    fun injectUserSignUpPresenter(presenter: ClientSignUpPresenter)
}