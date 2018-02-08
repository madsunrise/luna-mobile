package com.utrobin.luna.ui.presenter

import com.utrobin.luna.App
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.ui.contract.UserSignUpContract
import javax.inject.Inject

/**
 * Created by ivan on 08.02.2018.
 */

class UserSignUpPresenter : BasePresenter<UserSignUpContract.View>(), UserSignUpContract.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectUserSignUpPresenter(this)
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}