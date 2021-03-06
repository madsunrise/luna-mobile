package com.utrobin.luna.ui.contract

import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.model.Master
import com.utrobin.luna.network.NetworkError

interface MasterContract {

    interface View : MvpView {
        fun dataLoaded(master: Master)
        fun dataLoadingFailed(reason: NetworkError)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadData(master: FeedItem)
    }
}