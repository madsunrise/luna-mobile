package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.utrobin.luna.R
import com.utrobin.luna.adapter.FeedAdapter
import com.utrobin.luna.databinding.FeedFragmentBinding
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.FeedContract
import com.utrobin.luna.ui.presenter.FeedPresenter
import com.utrobin.luna.ui.utils.EndlessRecyclerOnScrollListener


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
        if (!adapterInitialized) {
            initializeAdapter()
        }
        setUpRecyclerView()
        binding.mainContainerSwipeToRefresh.setOnRefreshListener {
            requestDataUpdate()
        }

        binding.repeatBtn.setOnClickListener {
            requestDataUpdate()
        }
    }

    override fun dataLoaded(newItems: List<FeedItem>) {
        (activity as MainActivity).showProgressBar(false)
        isDataLoading = false
        feedAdapter.addItems(newItems)
        binding.mainContainerSwipeToRefresh.isRefreshing = false
    }

    override fun dataLoadingFailed(reason: NetworkError) {
        (activity as MainActivity).showProgressBar(false)
        binding.errorContainer.visibility = View.VISIBLE
        binding.mainContainerSwipeToRefresh.visibility = View.GONE
        binding.mainContainerSwipeToRefresh.isRefreshing = false
    }

    private fun initializeAdapter() {
        feedAdapter = FeedAdapter(ArrayList())
        feedAdapter.viewClickSubject.subscribe { presenter.onItemClicked(it) }
        feedAdapter.bookmarkClickSubject.subscribe {
            Toast.makeText(context, "Bookmark clicked for ${it.name}", Toast.LENGTH_SHORT).show()
            presenter.onBookmarkClicked(it)
        }
        presenter.loadInitialData()
        adapterInitialized = true
    }

    private fun requestDataUpdate() {
        (activity as MainActivity).showProgressBar(true)
        presenter.loadInitialData()
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.feedRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = feedAdapter
        recyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener(
                adapter = feedAdapter,
                linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                if (isDataLoading) {
                    return
                }
                isDataLoading = true
                presenter.loadMore(currentPage)
            }
        })
    }

    override fun navigateMasterScreen(item: FeedItem) {
        (activity as MainActivity).openMasterScreen(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    @VisibleForTesting
    fun getFeedItems() = feedAdapter.items

    companion object {
        fun getInstance() = FeedFragment()
    }
}