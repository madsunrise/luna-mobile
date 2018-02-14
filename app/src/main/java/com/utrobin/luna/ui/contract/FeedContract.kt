package com.utrobin.luna.ui.contract

import com.utrobin.luna.model.MasterBase
import com.utrobin.luna.network.NetworkError

/**
 * Created by ivan on 04.11.2017.
 */

interface FeedContract {

    interface View : MvpView {
        fun dataLoaded(newItems: List<MasterBase>, append: Boolean)
        fun dataLoadingFailed(reason: NetworkError)
    }


    interface Presenter : MvpPresenter<View> {
        fun loadInitialData()
        fun loadMore(page: Int)
    }
}