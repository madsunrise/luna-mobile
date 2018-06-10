package com.utrobin.luna.ui.view

import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.utrobin.luna.R
import com.utrobin.luna.model.MasterBase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val feedFragment = FeedFragment.getInstance()
    private val mapFragment = MapFragment.getInstance()
    private val accountFragment = CommonSignUpFragment.getInstance()

    private var previousFragment: Fragment? = null
    private var currentFragment: Fragment? = null


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
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
        setContentView(R.layout.activity_main)

        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

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

    fun openMasterScreen(item: MasterBase, sharedView: ViewPager) {
        val intent = Intent(this, MasterActivity::class.java)
        intent.apply {
            putExtra(MasterActivity.MASTER_BASE, item)
            putExtra(MasterActivity.TRANSITION_NAME, ViewCompat.getTransitionName(sharedView))
            putExtra(MasterActivity.CURRENT_PHOTO, sharedView.currentItem)
        }

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedView,
                ViewCompat.getTransitionName(sharedView)
        )

        startActivity(intent, options.toBundle())
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
        private const val FRAGMENT_TAG = "FRAGMENT_TAG"
    }
}
