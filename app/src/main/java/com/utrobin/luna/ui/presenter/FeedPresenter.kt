package com.utrobin.luna.ui.presenter

import com.apollographql.apollo.rx2.Rx2Apollo
import com.utrobin.luna.App
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.R
import com.utrobin.luna.model.Address
import com.utrobin.luna.model.Master
import com.utrobin.luna.model.Photo
import com.utrobin.luna.model.Sign
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.FeedContract
import com.utrobin.luna.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by ivan on 04.11.2017.
 */

class FeedPresenter : BasePresenter<FeedContract.View>(), FeedContract.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectsFeedPresenter(this)
    }

    override fun onItemClicked(item: Master) {
        view?.navigateMasterScreen(item)
    }

    override fun loadInitialData() {
        loadMore(1)
    }

    override fun loadMore(page: Int) {
        val query = FeedQuery
                .builder()
                .limit(10)
                .build()

        val apolloCall = graphQLService.apolloClient.query(query)
        Rx2Apollo.from(apolloCall)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { parseFeed(it.data()!!.feed()!!) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view?.dataLoaded(newItems = it, append = page != 1)
                        },
                        {
                            LogUtils.logException(FeedPresenter::class.java, it)
                            view?.dataLoadingFailed(NetworkError.UNKNOWN)
                        }
                )
    }


    private fun parseFeed(queryList: List<FeedQuery.Feed>): List<Master> {
        val data = ArrayList<Master>()
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

            val item = Master(name, avatar, address, stars, signs, photos)
            data.add(item)
        }
        return data
    }


    override fun onBookmarkClicked(item: Master) {
        if (item.isFavorite) {
            view?.showSnackBar(R.string.added_to_favorites)
        } else {
            view?.showSnackBar(R.string.removed_from_favorites)
        }
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}