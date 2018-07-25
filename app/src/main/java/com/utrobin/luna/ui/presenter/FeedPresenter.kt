package com.utrobin.luna.ui.presenter

import com.utrobin.luna.App
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.model.*
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.type.Limit
import com.utrobin.luna.ui.contract.FeedContract
import com.utrobin.luna.utils.LogUtils
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
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

        launch(UI) {
            try {
                val response = graphQLService.execute(apolloCall)
                val parsed = parseFeed(response.feed())
                view?.dataLoaded(newItems = parsed.await(), append = page != 1)
            } catch (e: Exception) {
                LogUtils.logException(FeedPresenter::class.java, e)
                view?.dataLoadingFailed(NetworkError.UNKNOWN)
            }
        }
    }


    private fun parseFeed(queryList: List<FeedQuery.Feed>) = async {
        val data = ArrayList<FeedItem>()
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
                                        it.id().toLong(),
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

            val type = when (queryItem.__typename()) {
                "Salon" -> FeedItem.Companion.Type.SALON
                "Master" -> FeedItem.Companion.Type.MASTER
                else -> throw IllegalArgumentException("Unsupported item type!")
            }

            data.add(
                    FeedItem(
                            id = id,
                            type = type,
                            name = name,
                            address = address,
                            avatar = avatar,
                            stars = stars,
                            signs = signs,
                            photos = photos,
                            ratesCount = queryItem.ratesCount(),
                            commentsCount = queryItem.commentsCount()
                    )
            )
        }
        return@async data
    }


    override fun destroy() {

    }

    companion object {
        private const val RECORDS_LIMIT = 10
    }
}