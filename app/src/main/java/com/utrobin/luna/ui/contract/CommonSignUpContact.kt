package com.utrobin.luna.ui.contract

interface CommonSignUpContact {
    interface View : MvpView {
        fun showSignUpAsUserFragment()
        fun showSignUpAsMasterFragment()
    }

    interface Presenter : MvpPresenter<View> {
        fun signUpAsUserSelected()
        fun signUpAsMasterSelected()
    }
}