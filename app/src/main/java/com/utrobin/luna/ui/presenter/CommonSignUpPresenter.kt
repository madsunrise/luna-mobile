package com.utrobin.luna.ui.presenter

import com.utrobin.luna.App
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.ui.contract.CommonSignUpContact
import javax.inject.Inject

class CommonSignUpPresenter : BasePresenter<CommonSignUpContact.View>(), CommonSignUpContact.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectCommonSignUpPresenter(this)
    }

    override fun signUpAsUserSelected() {
        view?.showSignUpAsUserFragment()
    }

    override fun signUpAsMasterSelected() {
        view?.showSignUpAsMasterFragment()
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}