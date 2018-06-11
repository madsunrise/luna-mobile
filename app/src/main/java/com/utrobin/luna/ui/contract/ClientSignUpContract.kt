package com.utrobin.luna.ui.contract

import com.utrobin.luna.network.NetworkError

interface ClientSignUpContract {
    interface View : MvpView {
        fun validateFields(): Boolean
        fun showProgressBar(show: Boolean)
        fun getUsername(): String
        fun getName(): String
        fun getEmail(): String
        fun getPassword(): String
        fun signUpFinished()
        fun signUpFailed(reason: NetworkError)
    }

    interface Presenter : MvpPresenter<View> {
        fun onSignUpButtonClicked()
        fun isLoginCorrect(login: String): Boolean
        fun isNameCorrect(name: String): Boolean
        fun isEmailCorrect(email: String): Boolean
        fun isPasswordCorrect(password: String): Boolean
    }
}