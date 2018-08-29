package com.utrobin.luna.ui.contract

import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.model.Salon
import com.utrobin.luna.network.NetworkError

interface SalonContract {

    interface View : MvpView {
        fun onDataLoaded(salon: Salon)
        fun onDataLoadingFailed(reason: NetworkError)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadData(salon: FeedItem)
    }
}