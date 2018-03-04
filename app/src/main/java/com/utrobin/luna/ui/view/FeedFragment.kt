package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utrobin.luna.R
import com.utrobin.luna.adapter.FeedAdapter
import com.utrobin.luna.databinding.FeedFragmentBinding
import com.utrobin.luna.model.MasterBase
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.FeedContract
import com.utrobin.luna.ui.presenter.FeedPresenter
import com.utrobin.luna.ui.utils.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.error_container.view.*

/**
 * Created by ivan on 01.11.2017.
 */

class FeedFragment : Fragment(), FeedContract.View {

    private lateinit var feedAdapter: FeedAdapter
    private var adapterInitialized = false
    private var isDataLoading = false

    lateinit var binding: FeedFragmentBinding

    private val presenter = FeedPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.feed_fragment, container, false)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setState(State.LOADING)
        if (!adapterInitialized) {
            initializeAdapter()
        }
        setUpRecyclerView()
        binding.mainContainerSwipeToRefresh.setOnRefreshListener {
            requestDataUpdate()
        }

        binding.errorContainer!!.repeat_btn.setOnClickListener {
            requestDataUpdate()
        }
    }

    override fun dataLoaded(newItems: List<MasterBase>, append: Boolean) {
        setState(State.CONTENT)
        binding.mainContainerSwipeToRefresh.isRefreshing = false
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
            (activity as MainActivity).openMasterScreen(it.first, it.second)
        }
        requestDataUpdate()
        adapterInitialized = true
    }

    private fun requestDataUpdate() {
        setState(State.LOADING)
        presenter.loadInitialData()
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.feedRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = feedAdapter
        onScrollListener = object : EndlessRecyclerOnScrollListener<MasterBase, ViewPager>(
                adapter = feedAdapter,
                linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(currentPage: Int): Boolean {
                if (isDataLoading) {
                    return false
                }
                isDataLoading = true
                presenter.loadMore(currentPage)
                return true
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun setState(state: State) {
        when (state) {
            State.CONTENT -> {
                with(binding) {
                    mainContainerSwipeToRefresh.visibility = View.VISIBLE
                    errorContainer!!.visibility = View.GONE
                    progressContainer!!.visibility = View.GONE
                }
            }
            State.ERROR -> {
                with(binding) {
                    mainContainerSwipeToRefresh.visibility = View.GONE
                    errorContainer!!.visibility = View.VISIBLE
                    progressContainer!!.visibility = View.GONE
                }
            }
            State.LOADING -> {
                with(binding) {
                    mainContainerSwipeToRefresh.visibility = View.GONE
                    errorContainer!!.visibility = View.GONE
                    progressContainer!!.visibility = View.VISIBLE
                }
            }
        }
    }

    private lateinit var onScrollListener: EndlessRecyclerOnScrollListener<MasterBase, ViewPager>

    @VisibleForTesting
    fun getFeedItems() = feedAdapter.items

    companion object {
        fun getInstance() = FeedFragment()
    }

    private enum class State {
        CONTENT, ERROR, LOADING
    }
}