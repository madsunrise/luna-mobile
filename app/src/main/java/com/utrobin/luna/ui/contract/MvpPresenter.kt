package com.utrobin.luna.ui.contract

interface MvpPresenter<T> where T : MvpView {
    fun attachView(mvpView: T)
    fun detachView()
    fun destroy()
}