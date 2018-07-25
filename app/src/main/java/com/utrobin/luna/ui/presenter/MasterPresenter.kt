package com.utrobin.luna.ui.presenter

import com.apollographql.apollo.rx2.Rx2Apollo
import com.utrobin.luna.App
import com.utrobin.luna.MasterQuery
import com.utrobin.luna.model.*
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.MasterContract
import com.utrobin.luna.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

        Rx2Apollo.from(apolloCall)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { parseData(master, it.data()!!.master()!!) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view?.dataLoaded(it)
                        },
                        {
                            LogUtils.logException(FeedPresenter::class.java, it)
                            view?.dataLoadingFailed(NetworkError.UNKNOWN)
                        }
                )
    }

    private fun parseData(base: FeedItem, data: MasterQuery.Master): Master {
        return Master(
                id = base.id,
                name = base.name,
                avatar = base.avatar,
                address = base.address,
                stars = base.stars,
                signs = base.signs,
                photos = base.photos,
                ratesCount = base.ratesCount,
                commentsCount = base.commentsCount,
                user = User(data.user()),
                salon = data.salon()?.let { Salon(it) },
                services = ArrayList(data.services().map { Service(it) }),
                schedules = ArrayList(data.schedules().map { Schedule(it) }),
                seances = ArrayList(data.seances().map { Seance(it) }),
                lastReviews = ArrayList(data.lastReviews().map { Review(it) })

        )
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}