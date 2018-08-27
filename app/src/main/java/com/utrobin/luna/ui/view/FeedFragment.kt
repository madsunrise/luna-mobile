package com.utrobin.luna.ui.view

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.utrobin.luna.R
import com.utrobin.luna.adapter.FeedAdapter
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.FeedContract
import com.utrobin.luna.ui.presenter.FeedPresenter
import com.utrobin.luna.ui.utils.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.error_container.view.*
import kotlinx.android.synthetic.main.feed_fragment.*

class FeedFragment : Fragment(), FeedContract.View {

    private lateinit var feedAdapter: FeedAdapter
    private var adapterInitialized = false
    private var isDataLoading = false

    private lateinit var presenter: FeedPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = FeedPresenter()
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setState(State.LOADING)
        if (!adapterInitialized) {
            initializeAdapter()
        }
        setUpRecyclerView()
        mainContainerSwipeToRefresh.setOnRefreshListener {
            requestDataUpdate()
        }

        errorContainer.repeatBtn.setOnClickListener {
            requestDataUpdate()
        }
    }

    override fun dataLoaded(newItems: List<FeedItem>, append: Boolean) {
        setState(State.CONTENT)
        mainContainerSwipeToRefresh.isRefreshing = false
        isDataLoading = false
        if (append) {
            feedAdapter.addItems(newItems)
        } else {
            feedAdapter.setItems(newItems)  // Full list update
            onScrollListener.resetVariables()
        }
    }

    override fun dataLoadingFailed(reason: NetworkError) {
        setState(State.ERROR)
    }

    private fun initializeAdapter() {
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)  // screen width in px
        feedAdapter = FeedAdapter(ArrayList(), displayMetrics.widthPixels)
        feedAdapter.viewClickSubject.subscribe {
            if (it.first.type == FeedItem.Companion.Type.SALON) {
                Toast.makeText(context, "Salons are not supported yet", Toast.LENGTH_SHORT).show()
            } else {
                (activity as MainActivity).openMasterScreen(it.first, it.second)
            }
        }
        requestDataUpdate()
        adapterInitialized = true
    }

    private fun requestDataUpdate() {
        setState(State.LOADING)
        presenter.loadInitialData()
    }

    private fun setUpRecyclerView() {
        feedRecyclerView.layoutManager = LinearLayoutManager(context)
        feedRecyclerView.adapter = feedAdapter
        onScrollListener = object : EndlessRecyclerOnScrollListener<FeedItem, ViewPager>(
                adapter = feedAdapter,
                linearLayoutManager = feedRecyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(currentPage: Int): Boolean {
                if (isDataLoading) {
                    return false
                }
                isDataLoading = true
                presenter.loadMore(currentPage)
                return true
            }
        }
        feedRecyclerView.addOnScrollListener(onScrollListener)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        presenter.destroy()
    }

    private fun setState(state: State) {
        when (state) {
            State.CONTENT -> {
                mainContainerSwipeToRefresh.visibility = View.VISIBLE
                errorContainer.visibility = View.GONE
                progressContainer.visibility = View.GONE
            }
            State.ERROR -> {
                mainContainerSwipeToRefresh.visibility = View.GONE
                errorContainer.visibility = View.VISIBLE
                progressContainer.visibility = View.GONE
            }
            State.LOADING -> {
                mainContainerSwipeToRefresh.visibility = View.GONE
                errorContainer.visibility = View.GONE
                progressContainer.visibility = View.VISIBLE
            }
        }
    }

    private lateinit var onScrollListener: EndlessRecyclerOnScrollListener<FeedItem, ViewPager>

    @VisibleForTesting
    fun getFeedItems() = feedAdapter.items

    companion object {
        fun getInstance() = FeedFragment()
    }

    private enum class State {
        CONTENT, ERROR, LOADING
    }
}