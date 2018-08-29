package com.utrobin.luna.ui.presenter

import com.utrobin.luna.App
import com.utrobin.luna.SalonQuery
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.model.Master
import com.utrobin.luna.model.Review
import com.utrobin.luna.model.Salon
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.SalonContract
import com.utrobin.luna.utils.LogUtils
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
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

        launch(UI) {
            try {
                val response = graphQLService.execute(apolloCall)
                view?.onDataLoaded(parseData(salon, response.salon()!!).await())
            } catch (e: Exception) {
                LogUtils.logException(FeedPresenter::class.java, e)
                view?.onDataLoadingFailed(NetworkError.UNKNOWN)
            }
        }
    }

    private fun parseData(base: FeedItem, data: SalonQuery.Salon) = async {
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
                masters = ArrayList(),
                lastReviews = ArrayList(data.fragments().additionalSalon().lastReviews().map { Review(it) })
        )

        salon.masters.addAll(data.fragments().additionalSalon().masters().map { Master(salon, it) })
        return@async salon
    }

    override fun destroy() {

    }
}