package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.utrobin.luna.R
import com.utrobin.luna.databinding.ActivityMainBinding
import com.utrobin.luna.model.MasterBase


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val feedFragment = FeedFragment.getInstance()
    private val mapFragment = MapFragment.getInstance()
    private val accountFragment = CommonSignUpFragment.getInstance()

    private var previousFragment: Fragment? = null
    private var currentFragment: Fragment? = null


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        if (currentFragment is MasterFragment) {
            supportFragmentManager.popBackStack()
            currentFragment = previousFragment
            previousFragment = null
        }
        when (it.itemId) {
            R.id.feed -> {
                if (currentFragment != feedFragment) {
                    showFragment(feedFragment)
                }
                true
            }
            R.id.map -> {
                if (currentFragment != mapFragment) {
                    showFragment(mapFragment)
                }
                true
            }
            R.id.account -> {
                if (currentFragment != accountFragment) {
                    showFragment(accountFragment)
                }
                true
            }
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)!!

        binding.bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        savedInstanceState?.let {
            currentFragment = supportFragmentManager.getFragment(savedInstanceState, FRAGMENT_TAG)
        } ?: showFragment(feedFragment)
    }


    private fun showFragment(to: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        if (currentFragment == null) {
            if (to.isAdded) {
                transaction.show(to)
            } else {
                transaction.add(R.id.container, to, to.tag)
            }
        } else {
            if (to.isAdded) {
                transaction.hide(currentFragment).show(to)
            } else {
                transaction.hide(currentFragment).add(R.id.container, to, to.tag)
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(to.tag)
        }
        transaction.commit()
        previousFragment = currentFragment
        currentFragment = to
    }

    fun openMasterScreen(item: MasterBase, sharedView: View) {
        val transitionName = ViewCompat.getTransitionName(sharedView)
        supportFragmentManager
                .beginTransaction()
                .addSharedElement(sharedView, transitionName)
                .addToBackStack(null)
                .replace(R.id.container, MasterFragment.getInstance(item, transitionName))
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {  // Returning from masterScreen
                supportFragmentManager.popBackStack()
                currentFragment = previousFragment
                previousFragment = null
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @VisibleForTesting
    fun getFeedItems() = feedFragment.getFeedItems()

    companion object {
        private val FRAGMENT_TAG = "FRAGMENT_TAG"
    }
}
