package com.utrobin.luna.ui.contract

/**
 * Created by ivan on 04.11.2017.
 */

interface MvpPresenter<T> where T : MvpView {

    fun attachView(mvpView: T)

    fun detachView()

    fun destroy()

}