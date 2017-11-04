package com.utrobin.luna.ui.contracts

import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.ui.utils.NetworkError

/**
 * Created by ivan on 04.11.2017.
 */

interface FeedContract {

    interface View : MvpView {
        fun dataLoaded(newItems: List<FeedItem>)
        fun dataLoadingFailed(reason: NetworkError)
    }


    interface Presenter : MvpPresenter<View> {
        fun onItemClicked(item: FeedItem)
        fun loadInitialData()
        fun loadMore(page: Int)
    }
}