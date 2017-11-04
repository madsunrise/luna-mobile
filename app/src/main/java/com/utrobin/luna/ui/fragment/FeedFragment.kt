package com.utrobin.luna.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.utrobin.luna.R
import com.utrobin.luna.adapter.FeedAdapter
import com.utrobin.luna.adapter.FooterLoaderAdapter
import com.utrobin.luna.model.Achievement
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.ui.utils.EndlessRecyclerOnScrollListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by ivan on 01.11.2017.
 */

class FeedFragment : Fragment() {

    @BindView(R.id.feed_recycler_view)
    lateinit var recyclerView: RecyclerView

    private lateinit var feedAdapter: FooterLoaderAdapter
    private var isDataLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.feed_fragment, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setItemViewCacheSize(20)
        feedAdapter = FeedAdapter(generateItems())
        recyclerView.adapter = feedAdapter
        feedAdapter.viewClickSubject.subscribe { Toast.makeText(context, it.location, Toast.LENGTH_SHORT).show() }

        recyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener(
                adapter = feedAdapter,
                linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                if (isDataLoading) {
                    return
                }
                isDataLoading = true
                Observable
                        .timer(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            feedAdapter.addItems(generateItems())
                            isDataLoading = false
                        }
            }
        })
    }

    private fun generateItems(): List<FeedItem> {
        val items = ArrayList<FeedItem>()
        val size = 5 //Random().nextInt(12) + 5
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

    companion object {
        private val TAG = FeedFragment::class.java.simpleName
    }
}