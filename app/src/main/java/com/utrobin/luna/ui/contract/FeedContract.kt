package com.utrobin.luna.ui.contract

import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.network.NetworkError

/**
 * Created by ivan on 04.11.2017.
 */

interface FeedContract {

    interface View : MvpView {
        fun dataLoaded(newItems: List<FeedItem>)
        fun dataLoadingFailed(reason: NetworkError)
        fun navigateMasterScreen(item: FeedItem)
    }


    interface Presenter : MvpPresenter<View> {
        fun onItemClicked(item: FeedItem)
        fun loadInitialData()
        fun loadMore(page: Int)
        fun onBookmarkClicked(item: FeedItem)
    }
}