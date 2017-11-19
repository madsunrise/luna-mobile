package com.utrobin.luna.ui.presenter

import android.util.Log
import com.apollographql.apollo.rx2.Rx2Apollo
import com.utrobin.luna.App
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.ui.contract.FeedContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

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
                        { it.data()?.feed?.let { parse(it) } },
                        { Log.e(TAG, "Exc", it) }
                )
    }


    private fun parse(list: List<FeedQuery.GetFeed>) {
        val data = ArrayList<FeedItem>()
        list
                .forEach {
                    val name = it.name() ?: "no name"
                    val avatar = it.avatar()?.path() ?: "no avatar"
                    val address = it.address()?.description() ?: "no address"
                    val photos = ArrayList<String>()
                    val stars = it.stars() ?: 0.0
                    it.photos()?.forEach {
                        it.path()?.let { photos.add(it) }
                    }

                    val item = FeedItem(name, avatar, address, ArrayList(), photos, stars)
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


    companion object {
        private val TAG = FeedPresenter::class.java.simpleName
    }
}