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

    override fun onSignUpButtonClicked() {
        if (view?.validateFields() != true) {
            return
        }
        // TODO make network request
    }

    override fun isLoginCorrect(login: String): Boolean {
        return login.length >= 5
    }

    override fun isNameCorrect(name: String): Boolean {
        return name.length >= 2
    }

    override fun isEmailCorrect(email: String): Boolean {
        return email.length >= 5
    }

    override fun isPasswordCorrect(password: String): Boolean {
        return password.length >= 6
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}