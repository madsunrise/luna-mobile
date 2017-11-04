package com.utrobin.luna.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.utrobin.luna.R
import com.utrobin.luna.adapter.FeedAdapter
import com.utrobin.luna.adapter.FooterLoaderAdapter
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.ui.contract.FeedContract
import com.utrobin.luna.ui.presenter.FeedPresenter
import com.utrobin.luna.ui.utils.EndlessRecyclerOnScrollListener
import com.utrobin.luna.ui.utils.NetworkError

/**
 * Created by ivan on 01.11.2017.
 */

class FeedFragment : Fragment(), FeedContract.View {

    @BindView(R.id.feed_recycler_view)
    lateinit var recyclerView: RecyclerView

    private lateinit var feedAdapter: FooterLoaderAdapter
    private var adapterInitialized = false
    private var isDataLoading = false

    private val presenter = FeedPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.feed_fragment, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!adapterInitialized) {
            initializeAdapter()
        }
        setUpRecyclerView()
    }

    override fun dataLoaded(newItems: List<FeedItem>) {
        (activity as MainActivity).showProgressBar(false)
        isDataLoading = false
        feedAdapter.addItems(newItems)
    }

    override fun dataLoadingFailed(reason: NetworkError) {
        Toast.makeText(context, R.string.error_has_occured, Toast.LENGTH_SHORT).show()
    }

    private fun initializeAdapter() {
        feedAdapter = FeedAdapter(ArrayList())
        feedAdapter.viewClickSubject.subscribe { presenter.onItemClicked(it) }
        presenter.loadInitialData()
        adapterInitialized = true
    }

    private fun setUpRecyclerView() {
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

    companion object {
        private val TAG = FeedFragment::class.java.simpleName
    }
}