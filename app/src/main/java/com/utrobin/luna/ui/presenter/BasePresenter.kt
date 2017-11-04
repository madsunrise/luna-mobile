package com.utrobin.luna.ui.presenter

import com.utrobin.luna.ui.contract.MvpPresenter
import com.utrobin.luna.ui.contract.MvpView


/**
 * Created by ivan on 04.11.2017.
 */

abstract class BasePresenter<T : MvpView> : MvpPresenter<T> {

    var view: T? = null
        private set

    protected val isViewAttached: Boolean
        get() = view != null

    override fun attachView(mvpView: T) {
        view = mvpView
    }

    override fun detachView() {
        view = null
    }
}