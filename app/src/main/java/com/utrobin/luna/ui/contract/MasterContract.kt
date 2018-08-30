package com.utrobin.luna.ui.contract

import com.utrobin.luna.entity.FeedItem
import com.utrobin.luna.entity.Master
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