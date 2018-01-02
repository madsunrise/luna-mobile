package com.utrobin.luna.ui.presenter

import com.apollographql.apollo.rx2.Rx2Apollo
import com.utrobin.luna.App
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.model.Address
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.model.Photo
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


    private fun parseFeed(list: List<FeedQuery.Feed>) {
        val data = ArrayList<FeedItem>()
        list.forEach {
            val name = it.name() ?: throw NullPointerException("No name provided!")
            val avatar = Photo(it.avatar() ?: throw NullPointerException("No avatar provided!"))
            val address = Address(it.address() ?: throw NullPointerException("No name provided!"))
            val stars = it.stars() ?: throw NullPointerException("No stars provided!")
            val photos = ArrayList<Photo>()
            it.photos()?.forEach {
                photos.add(Photo(it))
            }

            val item = FeedItem(name, avatar, address, stars, ArrayList(), photos)
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