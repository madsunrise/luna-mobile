package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.utrobin.luna.App
import com.utrobin.luna.R
import com.utrobin.luna.databinding.ActivityMainBinding
import com.utrobin.luna.model.FeedItem


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val feedFragment = FeedFragment()
    private val mapFragment = MapFragment()
    private val accountFragment = AccountFragment()

    private var previousFragment: Fragment? = null
    private var currentFragment: Fragment? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var from = currentFragment
        if (currentFragment is MasterFragment) {
            supportFragmentManager.popBackStack()
            from = previousFragment
        }
        when (item.itemId) {
            R.id.feed -> {
                if (from != feedFragment) {
                    showFragment(from, feedFragment)
                }
                true
            }
            R.id.map -> {
                if (from != mapFragment) {
                    showFragment(from, mapFragment)
                }
                true
            }
            R.id.account -> {
                if (from != accountFragment) {
                    showFragment(from, accountFragment)
                }
                true
            }
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        App.component.injectsMainActivity(this)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        savedInstanceState?.let {
            currentFragment = supportFragmentManager.getFragment(savedInstanceState, FRAGMENT_TAG)
        } ?: showFragment(null, feedFragment)
    }


    private fun showFragment(from: Fragment?, to: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        if (from == null) {
            if (to.isAdded) {
                transaction.show(to)
            } else {
                transaction.add(R.id.container, to, to.tag)
            }
        } else {
            if (to.isAdded) {
                transaction.hide(from).show(to)
            } else {
                transaction.hide(from).add(R.id.container, to, to.tag)
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(to.tag)
        }
        transaction.commitAllowingStateLoss()
        previousFragment = currentFragment
        currentFragment = to
    }


    fun openMasterScreen(item: FeedItem) {
        showProgressBar(true)
        showFragment(currentFragment, MasterFragment(), true)
    }

    fun showProgressBar(show: Boolean) {
        if (show) {
            binding.container.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.container.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }


    companion object {
        private val TAG = MainActivity::javaClass.javaClass.simpleName
        private val FRAGMENT_TAG = TAG + "FRAGMENT_TAG"
    }
}
