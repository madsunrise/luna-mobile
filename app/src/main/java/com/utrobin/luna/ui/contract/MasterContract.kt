package com.utrobin.luna.ui.contract

/**
 * Created by ivan on 04.11.2017.
 */

interface MasterContract {

    interface View : MvpView {

    }

    interface Presenter : MvpPresenter<View> {

    }
}