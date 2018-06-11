package com.utrobin.luna.ui.presenter

import com.apollographql.apollo.rx2.Rx2Apollo
import com.utrobin.luna.App
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.model.*
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.type.Limit
import com.utrobin.luna.ui.contract.FeedContract
import com.utrobin.luna.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FeedPresenter : BasePresenter<FeedContract.View>(), FeedContract.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectFeedPresenter(this)
    }


    override fun loadInitialData() {
        loadMore(1)
    }

    override fun loadMore(page: Int) {
        val query = FeedQuery
                .builder()
                .limit(Limit.builder()
                        .limit(RECORDS_LIMIT)
                        .offset((page - 1) * RECORDS_LIMIT)
                        .build()
                )
                .build()

        val apolloCall = graphQLService.apolloClient.query(query)
        Rx2Apollo.from(apolloCall)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { parseFeed(it.data()!!.feed()) }
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


    private fun parseFeed(queryList: List<FeedQuery.Feed>): List<MasterBase> {
        val data = ArrayList<MasterBase>()
        for (queryItem in queryList) {
            val id = queryItem.id().toLong()
            val name = queryItem.name()
            val avatar = queryItem.avatar()?.let { Photo(it) }
            val stars = queryItem.stars() / 10.0

            val photos = ArrayList<Photo>()
            queryItem.photos().forEach {
                photos.add(Photo(it))
            }

            val signs = ArrayList<Sign>()
            queryItem.signs().forEach {
                signs.add(Sign(it))
            }

            val addressMetro = ArrayList<AddressMetro>()

            queryItem.address()?.metros()
                    ?.forEach {
                        addressMetro.add(
                                AddressMetro(
                                        it.station(),
                                        it.line(),
                                        it.color(),
                                        it.distance().toFloat()
                                )
                        )
                    }

            val address = queryItem.address()?.let {
                Address(
                        it.description(),
                        it.lat(),
                        it.lon(),
                        addressMetro
                )
            }


            data.add(
                    MasterBase(
                            id = id,
                            name = name,
                            address = address,
                            avatar = avatar,
                            stars = stars,
                            signs = signs,
                            photos = photos
                    )
            )
        }
        return data
    }


    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val RECORDS_LIMIT = 10
    }
}