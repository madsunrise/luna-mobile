package com.utrobin.luna.ui.contract

import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.model.Master
import com.utrobin.luna.network.NetworkError

interface MasterContract {

    interface View : MvpView {
        fun onDataLoaded(master: Master)
        fun onDataLoadingFailed(reason: NetworkError)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadData(master: FeedItem)
    }
}