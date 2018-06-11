package com.utrobin.luna.ui.contract

import com.utrobin.luna.model.MasterBase
import com.utrobin.luna.model.MasterExtended
import com.utrobin.luna.network.NetworkError

interface MasterContract {

    interface View : MvpView {
        fun dataLoaded(master: MasterExtended)
        fun dataLoadingFailed(reason: NetworkError)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadData(master: MasterBase)
    }
}