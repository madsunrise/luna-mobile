package com.utrobin.luna.ui.contract

/**
 * Created by ivan on 21.01.2018.
 */
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