package com.utrobin.luna.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.utrobin.luna.R
import com.utrobin.luna.adapter.FeedAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var feedAdapter: FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.feed_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        feedAdapter = FeedAdapter(ArrayList())
        recyclerView.adapter = feedAdapter
        feedAdapter.viewClickSubject.subscribe { Toast.makeText(this, it.location, Toast.LENGTH_SHORT).show() }
    }
}
