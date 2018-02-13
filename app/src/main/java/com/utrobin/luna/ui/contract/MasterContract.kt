package com.utrobin.luna.ui.contract

import com.utrobin.luna.model.MasterBase
import com.utrobin.luna.network.NetworkError

/**
 * Created by ivan on 04.11.2017.
 */

interface MasterContract {

    interface View : MvpView {
        fun dataLoaded(masterBase: MasterBase)
        fun dataLoadingFailed(reason: NetworkError)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadData(userId: Long)
        fun onBookmarkClicked()
    }
}