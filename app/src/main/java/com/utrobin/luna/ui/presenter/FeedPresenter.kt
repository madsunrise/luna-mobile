package com.utrobin.luna.ui.presenter

import android.util.Log
import com.utrobin.luna.model.Achievement
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.ui.contracts.FeedContract
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

    }

    override fun loadInitialData() {
        Observable
                .timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view?.dataLoaded(generateItems(20)) },
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
            when (Random().nextInt() % 6) {
                0 -> achievements.add(Achievement.CAREFUL)
                1 -> achievements.add(Achievement.FRIENDLY)
                2 -> achievements.add(Achievement.FAST)
                3 -> {
                    achievements.add(Achievement.FAST)
                    achievements.add(Achievement.FRIENDLY)
                    achievements.add(Achievement.FAST)
                }
                4 -> {
                    achievements.add(Achievement.CAREFUL)
                    achievements.add(Achievement.FAST)
                    achievements.add(Achievement.CAREFUL)
                }
                5 -> {
                    achievements.add(Achievement.FAST)
                    achievements.add(Achievement.CAREFUL)
                    achievements.add(Achievement.FRIENDLY)
                }
                else -> {
                    achievements.add(Achievement.CAREFUL)
                }
            }
            Log.d(TAG, "Loop: $i Achievements size = ${achievements.size}")
            val item = FeedItem("Салон Jasmine. Мастер Евгения", "Рядом с метро Курская", achievements)
            items.add(item)
        }

        return items
    }

    override fun destroy() {

    }


    companion object {
        private val TAG = FeedPresenter::class.java.simpleName
    }
}