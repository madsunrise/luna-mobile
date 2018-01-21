package com.utrobin.luna.ui.contract

import com.utrobin.luna.model.Master
import com.utrobin.luna.network.NetworkError

/**
 * Created by ivan on 04.11.2017.
 */

interface MasterContract {

    interface View : MvpView {
        fun dataLoaded(master: Master)
        fun dataLoadingFailed(reason: NetworkError)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadData(userId: Long)
        fun onBookmarkClicked()
    }
}