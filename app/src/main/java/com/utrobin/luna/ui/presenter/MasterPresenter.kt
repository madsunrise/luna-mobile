package com.utrobin.luna.ui.presenter

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.utrobin.luna.App
import com.utrobin.luna.MasterQuery
import com.utrobin.luna.entity.*
import com.utrobin.luna.model.ExecuteApolloCallJob
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.MasterContract
import javax.inject.Inject

class MasterPresenter : BasePresenter<MasterContract.View>(), MasterContract.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectMasterPresenter(this)
    }

    override fun loadData(master: FeedItem) {
        val query = MasterQuery
                .builder()
                .id(master.id.toString())
                .build()

        val apolloCall = graphQLService.apolloClient.query(query)

        ExecuteApolloCallJob<MasterQuery.Data>(graphQLService, apolloCall).execute(
                {
                    view?.onDataLoaded(parseData(master, it.master()!!))
                },
                {
                    Log.e(TAG, "Error", it)
                    Crashlytics.logException(it)
                    view?.onDataLoadingFailed(NetworkError.UNKNOWN)
                }
        )
    }

    private fun parseData(base: FeedItem, data: MasterQuery.Master) =
            Master(
                    id = base.id,
                    name = base.name,
                    avatar = base.avatar,
                    address = base.address,
                    stars = base.stars,
                    signs = base.signs,
                    photos = base.photos,
                    ratesCount = base.ratesCount,
                    commentsCount = base.commentsCount,
                    services = base.services,
                    user = User(data.fragments().additionalMaster().user()),
                    salon = null, // TODO will be salon here? It's name or what?
                    schedules = ArrayList(data.fragments().additionalMaster().schedules().map { Schedule(it) }),
                    seances = ArrayList(data.fragments().additionalMaster().seances().map { Seance(it) }),
                    lastReviews = ArrayList(data.fragments().additionalMaster().lastReviews().map { Review(it) })
            )

    override fun destroy() {

    }

    companion object {
        private val TAG = MasterPresenter::class.java.simpleName
    }
}