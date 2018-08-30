package com.utrobin.luna.ui.presenter

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.utrobin.luna.App
import com.utrobin.luna.SalonQuery
import com.utrobin.luna.entity.FeedItem
import com.utrobin.luna.entity.Master
import com.utrobin.luna.entity.Review
import com.utrobin.luna.entity.Salon
import com.utrobin.luna.model.ExecuteApolloCallJob
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.SalonContract
import javax.inject.Inject

class SalonPresenter : BasePresenter<SalonContract.View>(), SalonContract.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectSalonPresenter(this)
    }

    override fun loadData(salon: FeedItem) {
        val query = SalonQuery
                .builder()
                .id(salon.id.toString())
                .service_types(null)
                .build()

        val apolloCall = graphQLService.apolloClient.query(query)

        ExecuteApolloCallJob<SalonQuery.Data>(graphQLService, apolloCall).execute(
                {
                    view?.onDataLoaded(parseData(salon, it.salon()!!))
                },
                {
                    Log.e(TAG, "Error", it)
                    Crashlytics.logException(it)
                    view?.onDataLoadingFailed(NetworkError.UNKNOWN)
                }
        )
    }

    private fun parseData(base: FeedItem, data: SalonQuery.Salon): Salon {

        val signsTotalStr = data.fragments().additionalSalon().signsTotal()
        val signsTotal: Map<Long, Int> = Gson().fromJson(signsTotalStr, object : TypeToken<Map<Long, Int>>() {}.type)

        val salon = Salon(
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
                signsTotal = signsTotal,
                masters = ArrayList(),
                lastReviews = ArrayList(data.fragments().additionalSalon().lastReviews().map { Review(it) })
        )

        salon.masters.addAll(data.fragments().additionalSalon().masters().map { Master(salon, it) })
        return salon
    }

    override fun destroy() {

    }

    companion object {
        private val TAG = SalonPresenter::class.java.simpleName
    }
}