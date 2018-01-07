package com.utrobin.luna.ui.contract

import android.support.design.widget.Snackbar
import com.utrobin.luna.model.Master
import com.utrobin.luna.network.NetworkError

/**
 * Created by ivan on 04.11.2017.
 */

interface FeedContract {

    interface View : MvpView {
        fun dataLoaded(newItems: List<Master>, append: Boolean)
        fun dataLoadingFailed(reason: NetworkError)
        fun navigateMasterScreen(item: Master)
        fun showSnackBar(text: Int, length: Int = Snackbar.LENGTH_SHORT)
    }


    interface Presenter : MvpPresenter<View> {
        fun onItemClicked(item: Master)
        fun loadInitialData()
        fun loadMore(page: Int)
        fun onBookmarkClicked(item: Master)
    }
}