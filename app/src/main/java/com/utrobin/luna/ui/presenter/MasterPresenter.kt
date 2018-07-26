package com.utrobin.luna.ui.presenter

import com.utrobin.luna.App
import com.utrobin.luna.MasterQuery
import com.utrobin.luna.model.*
import com.utrobin.luna.network.GraphQLService
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.MasterContract
import com.utrobin.luna.utils.LogUtils
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
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

        launch(UI) {
            try {
                val response = graphQLService.execute(apolloCall)
                view?.dataLoaded(parseData(master, response.master()!!).await())
            } catch (e: Exception) {
                LogUtils.logException(FeedPresenter::class.java, e)
                view?.dataLoadingFailed(NetworkError.UNKNOWN)
            }

        }
    }

    private fun parseData(base: FeedItem, data: MasterQuery.Master) = async {
        return@async Master(
                id = base.id,
                name = base.name,
                avatar = base.avatar,
                address = base.address,
                stars = base.stars,
                signs = base.signs,
                photos = base.photos,
                ratesCount = base.ratesCount,
                commentsCount = base.commentsCount,
                user = User(data.fragments().additionalMaster().user()),
                salon = data.fragments().additionalMaster().salon()?.let { Salon(it) },
                services = ArrayList(data.fragments().additionalMaster().services().map { Service(it) }),
                schedules = ArrayList(data.fragments().additionalMaster().schedules().map { Schedule(it) }),
                seances = ArrayList(data.fragments().additionalMaster().seances().map { Seance(it) }),
                lastReviews = ArrayList(data.fragments().additionalMaster().lastReviews().map { Review(it) })
        )
    }

    override fun destroy() {

    }
}