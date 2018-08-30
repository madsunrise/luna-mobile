package com.utrobin.luna.ui.contract

import com.utrobin.luna.entity.FeedItem
import com.utrobin.luna.network.NetworkError

interface FeedContract {

    interface View : MvpView {
        fun dataLoaded(newItems: List<FeedItem>, append: Boolean)
        fun dataLoadingFailed(reason: NetworkError)
    }


    interface Presenter : MvpPresenter<View> {
        fun loadInitialData()
        fun loadMore(page: Int)
    }
}