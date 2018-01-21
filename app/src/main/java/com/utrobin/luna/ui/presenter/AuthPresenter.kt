package com.utrobin.luna.ui.presenter

import com.utrobin.luna.App
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.ui.contract.AuthContract
import javax.inject.Inject

/**
 * Created by ivan on 21.01.2018.
 */

class AuthPresenter : BasePresenter<AuthContract.View>(), AuthContract.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectAuthPresenter(this)
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}