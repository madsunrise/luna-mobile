package com.utrobin.luna.ui.presenter

import com.utrobin.luna.App
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.ui.contract.MasterSignUpContract
import javax.inject.Inject

/**
 * Created by ivan on 08.02.2018.
 */

class MasterSignUpPresenter : BasePresenter<MasterSignUpContract.View>(), MasterSignUpContract.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectMasterSignUpPresenter(this)
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}