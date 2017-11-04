package com.utrobin.luna.ui.view

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.utrobin.luna.App
import com.utrobin.luna.R
import com.utrobin.luna.model.FeedItem


class MainActivity : AppCompatActivity() {

    @BindView(R.id.container)
    lateinit var container: FrameLayout

    @BindView(R.id.progress_bar)
    lateinit var progressBar: ProgressBar


    @BindView(R.id.bottom_navigation)
    lateinit var navigation: BottomNavigationView

    private val feedFragment = FeedFragment()
    private val mapFragment = MapFragment()

    private var currentFragment: Fragment? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (currentFragment is MasterFragment) {
            supportFragmentManager.popBackStack()
            currentFragment = feedFragment          // currentFragment may contain wrong value
            return@OnNavigationItemSelectedListener true
        }
        when (item.itemId) {
            R.id.feed -> {
                changeFragment(feedFragment, false)
                true
            }
            R.id.map -> {
                changeFragment(mapFragment, false)
                true
            }
            R.id.account -> true
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        App.component.injectsMainActivity(this)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        savedInstanceState?.let {
            currentFragment = supportFragmentManager.getFragment(savedInstanceState, FRAGMENT_TAG)
        } ?: changeFragment(feedFragment, false)
    }

    private fun changeFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(container.id, fragment, FRAGMENT_TAG);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.tag);
        }
        transaction.commitAllowingStateLoss();
        currentFragment = fragment;
    }

    fun openMasterScreen(item: FeedItem) {
        changeFragment(MasterFragment(), true)
    }

    fun showProgressBar(show: Boolean) {
        if (show) {
            container.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            container.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }


    companion object {
        private val TAG = MainActivity::javaClass.javaClass.simpleName
        private val FRAGMENT_TAG = TAG + "FRAGMENT_TAG"
    }
}
