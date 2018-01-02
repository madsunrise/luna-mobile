package com.utrobin.luna.ui.presenter

import com.apollographql.apollo.rx2.Rx2Apollo
import com.utrobin.luna.App
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.model.Address
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.model.Photo
import com.utrobin.luna.model.Sign
import com.utrobin.luna.ui.contract.FeedContract
import com.utrobin.luna.ui.utils.NetworkError
import com.utrobin.luna.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by ivan on 04.11.2017.
 */

class FeedPresenter : BasePresenter<FeedContract.View>(), FeedContract.Presenter {

    override fun onItemClicked(item: FeedItem) {
        view?.navigateMasterScreen(item)
    }

    override fun loadInitialData() {
        val query = FeedQuery
                .builder()
                .limit(10)
                .build()

        val apolloCall = App.apolloClient.query(query)
        Rx2Apollo.from(apolloCall)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { it.data()?.feed()?.let { parseFeed(it) } ?: view?.dataLoadingFailed(NetworkError.UNKNOWN) },
                        {
                            LogUtils.logException(FeedPresenter::class.java, it)
                            view?.dataLoadingFailed(NetworkError.UNKNOWN)
                        }
                )
    }


    private fun parseFeed(queryList: List<FeedQuery.Feed>) {
        val data = ArrayList<FeedItem>()
        for (queryItem in queryList) {
            val name = queryItem.name() ?: continue
            val avatar = Photo(queryItem.avatar() ?: continue)
            val address = Address(queryItem.address() ?: continue)
            val stars = queryItem.stars() ?: continue

            val photos = ArrayList<Photo>()
            queryItem.photos()?.forEach {
                photos.add(Photo(it))
            }

            val signs = ArrayList<Sign>()
            queryItem.signs()?.forEach {
                signs.add(Sign(it))
            }

            val item = FeedItem(name, avatar, address, stars, signs, photos)
            data.add(item)
        }
        view?.dataLoaded(data)
    }

    override fun loadMore(page: Int) {
        loadInitialData()
    }


    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}