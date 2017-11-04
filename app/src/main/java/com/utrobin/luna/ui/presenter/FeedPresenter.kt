package com.utrobin.luna.ui.presenter

import android.util.Log
import com.utrobin.luna.model.Achievement
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.ui.contract.FeedContract
import com.utrobin.luna.ui.utils.NetworkError
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by ivan on 04.11.2017.
 */

class FeedPresenter : BasePresenter<FeedContract.View>(), FeedContract.Presenter {

    override fun onItemClicked(item: FeedItem) {
        view?.navigateMasterScreen(item)
    }

    override fun loadInitialData() {
        Observable
                .timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view?.dataLoaded(generateItems(7)) },
                        { ex ->
                            Log.e(TAG, ex.message)
                            view?.dataLoadingFailed(NetworkError.UNKNOWN)
                        }
                )
    }

    override fun loadMore(page: Int) {
        Observable
                .timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view?.dataLoaded(generateItems()) },
                        { ex ->
                            Log.e(TAG, ex.message)
                            view?.dataLoadingFailed(NetworkError.UNKNOWN)
                        }
                )
    }


    private fun generateItems(size: Int = 5): List<FeedItem> {
        val items = ArrayList<FeedItem>()
        for (i in 0 until size) {
            val achievements = ArrayList<Achievement>()
            val achivsCount = Random().nextInt(4) + 1
            for (k in 1 .. achivsCount) {
                achievements.add(Achievement.values()[Random().nextInt(Achievement.values().size)])
            }
            val item = FeedItem("Салон Jasmine. Мастер Евгения", "Рядом с метро Курская", achievements)
            items.add(item)
        }

        return items
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        private val TAG = FeedPresenter::class.java.simpleName
    }
}