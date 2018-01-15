package com.utrobin.luna.ui.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.utrobin.luna.adapter.FooterLoaderAdapter

/**
 * Created by ivan on 31.10.2017.
 */

abstract class EndlessRecyclerOnScrollListener<T>(
        private val adapter: FooterLoaderAdapter<T>,
        private val linearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private val visibleThreshold = 2
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var currentPage = 2    // Первая страница загрузилась при запуске

    fun resetVariables() {
        previousTotal = 0
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
        currentPage = 2
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = linearLayoutManager.itemCount
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

        if (adapter.loading) {
            if (totalItemCount > previousTotal) {
                adapter.loading = false
                previousTotal = totalItemCount
            }
        }
        if (!adapter.loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            if (onLoadMore(currentPage)) {
                currentPage++
            }
            adapter.loading = true
        }
    }

    /**
     * @currentPage - страница, которую надо загрузить
     * @return true если загрузка началась и false, если нет (например, загрузка и так идет)
     */
    abstract fun onLoadMore(currentPage: Int): Boolean
}