package com.utrobin.luna.ui.contract

import com.utrobin.luna.entity.FeedItem
import com.utrobin.luna.entity.Salon
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