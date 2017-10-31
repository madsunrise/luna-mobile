package com.utrobin.luna.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.utrobin.luna.R
import com.utrobin.luna.adapter.FeedAdapter
import com.utrobin.luna.model.Achievement
import com.utrobin.luna.model.FeedItem
import java.util.*


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
        recyclerView.setItemViewCacheSize(20)
        feedAdapter = FeedAdapter(generateItems())
        recyclerView.adapter = feedAdapter
        feedAdapter.viewClickSubject.subscribe { Toast.makeText(this, it.location, Toast.LENGTH_SHORT).show() }
    }

    private fun generateItems(): List<FeedItem> {
        val items = ArrayList<FeedItem>()
        for (i in 1..30) {
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
                else -> { achievements.add(Achievement.CAREFUL)
                }
            }
            Log.d(TAG, "Loop: $i Achievements size = ${achievements.size}")
            val item = FeedItem("Салон Jasmine. Мастер Евгения", "Рядом с метро Курская", achievements)
            items.add(item)
        }

        return items
    }

    companion object {
        val TAG = MainActivity::javaClass.javaClass.simpleName
    }
}
