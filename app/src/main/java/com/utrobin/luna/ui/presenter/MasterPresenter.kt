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

/**
 * Created by ivan on 04.11.2017.
 */

class MasterPresenter : BasePresenter<MasterContract.View>(), MasterContract.Presenter {

    @Inject
    lateinit var graphQLService: GraphQLService

    init {
        App.component.injectMasterPresenter(this)
    }

    override fun loadData(userId: Long) {
        val query = MasterQuery
                .builder()
                .id(userId.toString())
                .build()

        val apolloCall = graphQLService.apolloClient.query(query)

        Rx2Apollo.from(apolloCall)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { parseDate(it.data()!!.master()!!) }
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

    private fun parseDate(data: MasterQuery.Master): Master {
        val name = data.name()

        val tags = ArrayList<Tag>()
        data.avatar().tags().forEach {
            tags.add(Tag(it.name()))
        }
        val avatar = Photo(data.avatar().path(), tags)

        val addressDesc = data.address().description()
        val lat = data.address().lat()
        val lon = data.address().lon()
        val address = Address(addressDesc, lat, lon)

        val stars = data.stars()

        val signs = ArrayList<Sign>()
        data.signs().forEach {
            signs.add(Sign(it.name(), it.description(), it.icon()))
        }

        val photos = ArrayList<Photo>()
        data.photos().forEach {
            val photoTags = ArrayList<Tag>()
            it.tags().forEach {
                photoTags.add(Tag(it.name()))
            }
            photos.add(Photo(it.path(), photoTags))
        }

        return Master(
                name = name,
                avatar = avatar,
                address = address,
                stars = stars,
                photos = photos
        )
    }

    override fun onBookmarkClicked() {

    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}