package com.utrobin.luna.ui.presenter

import com.utrobin.luna.App
import com.utrobin.luna.MasterQuery
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.ui.contract.MasterContract
import javax.inject.Inject

/**
 * Created by ivan on 04.11.2017.
 */

class MasterPresenter : BasePresenter<MasterContract.View>(), MasterContract.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectMasterPresenter(this)
    }

    override fun loadData(userId: Long) {
        val query = MasterQuery
                .builder()
                .id(userId.toString())
                .build()

        val apolloCall = graphQLService.apolloClient.query(query)

        // TODO what do we want to receive?
//        Rx2Apollo.from(apolloCall)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        {
//                            view?.dataLoaded(it)
//                        },
//                        {
//                            LogUtils.logException(FeedPresenter::class.java, it)
//                            view?.dataLoadingFailed(NetworkError.UNKNOWN)
//                        }
//                )
    }


    override fun onBookmarkClicked() {

    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}