package com.utrobin.luna.ui.contract

/**
 * Created by ivan on 08.02.2018.
 */

interface MasterSignUpContract {
    interface View : MvpView {
        fun validateFields(): Boolean
        fun showProgressBar(show: Boolean)
        fun showErrorMsg(msg: String)
        fun showSuccessfulMessage()
    }

    interface Presenter : MvpPresenter<View> {
        fun onSignUpButtonClicked()
        fun isLoginCorrect(login: String): Boolean
        fun isNameCorrect(name: String): Boolean
        fun isEmailCorrect(email: String): Boolean
        fun isPasswordCorrect(password: String): Boolean
    }
}