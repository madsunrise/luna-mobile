package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.utrobin.luna.R
import com.utrobin.luna.adapter.FeedAdapter
import com.utrobin.luna.adapter.FooterLoaderAdapter
import com.utrobin.luna.databinding.FeedFragmentBinding
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.ui.contract.FeedContract
import com.utrobin.luna.ui.presenter.FeedPresenter
import com.utrobin.luna.ui.utils.EndlessRecyclerOnScrollListener
import com.utrobin.luna.ui.utils.NetworkError


/**
 * Created by ivan on 01.11.2017.
 */

class FeedFragment : Fragment(), FeedContract.View {

    private lateinit var feedAdapter: FooterLoaderAdapter
    private var adapterInitialized = false
    private var isDataLoading = false

    lateinit var binding: FeedFragmentBinding

    private val presenter = FeedPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.feed_fragment, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!adapterInitialized) {
            initializeAdapter()
        }
        setUpRecyclerView()
        binding.swipeRefreshLayout.setOnRefreshListener {
            (activity as MainActivity).showProgressBar(true)
            presenter.loadInitialData()
        }
    }

    override fun dataLoaded(newItems: List<FeedItem>) {
        (activity as MainActivity).showProgressBar(false)
        isDataLoading = false
        feedAdapter.addItems(newItems)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun dataLoadingFailed(reason: NetworkError) {
        Toast.makeText(context, R.string.error_has_occured, Toast.LENGTH_SHORT).show()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun initializeAdapter() {
        feedAdapter = FeedAdapter(ArrayList())
        feedAdapter.viewClickSubject.subscribe { presenter.onItemClicked(it) }
        presenter.loadInitialData()
        adapterInitialized = true
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

    override fun onResume() {
        super.onResume()
    }
}