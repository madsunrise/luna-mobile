package com.utrobin.luna.ui.view

import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.model.Salon
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.SalonContract
import com.utrobin.luna.ui.presenter.SalonPresenter
import kotlinx.android.synthetic.main.master_activity.*

class SalonActivity : AppCompatActivity(), SalonContract.View {
    private lateinit var salon: Salon

    private val presenter = SalonPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.salon_activity)

        presenter.attachView(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val base = intent.extras.getParcelable<FeedItem>(SALON_BASE)
        title = base.name

        setState(State.LOADING)

        presenter.loadData(base)
//        errorContainer!!.repeat_btn.setOnClickListener {
//            setState(State.LOADING)
//            presenter.loadData(base)
//        }

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        imageSlider.layoutParams.height = displayMetrics.widthPixels * 9 / 16

        val adapter = ViewPagerAdapter(this, base.photos)
        imageSlider.adapter = adapter
        adapter.imageClickSubject.subscribe {
            //openImageViewer()
        }


        supportPostponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageSlider.transitionName = intent.extras.getString(TRANSITION_NAME)
            imageSlider.currentItem = intent.extras.getInt(CURRENT_PHOTO)
        }
        supportStartPostponedEnterTransition()


        buttonWhat.setOnClickListener { showServicesBlock(true) }
        closeServicesBtn.setOnClickListener { showServicesBlock(false) }

        val totalPhotos = base.photos.size
        imageSlider.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                photosCounter.text = getString(R.string.master_photos_counter_template, position + 1, totalPhotos)
            }

            override fun onPageSelected(position: Int) {

            }
        })
    }

    companion object {
        const val SALON_BASE = "SALON_BASE_EXTRA"
        const val TRANSITION_NAME = "TRANSITION_NAME_EXTRA"
        const val CURRENT_PHOTO = "CURRENT_PHOTO_EXTRA"
    }

    private enum class State {
        CONTENT, ERROR, LOADING
    }

    override fun onDataLoaded(salon: Salon) {
        setState(State.CONTENT)
        this.salon = salon
        fillViews()
    }

    override fun onDataLoadingFailed(reason: NetworkError) {
        setState(State.ERROR)
    }

    private fun fillViews() {

    }


    // TODO make loading and error state
    private fun setState(state: State) {
//        when (state) {
//            State.CONTENT -> {
//                with( {
//                    downloadableContent.visibility = View.VISIBLE
//                    errorContainer.visibility = View.GONE
//                    progressBar.visibility = View.GONE
//                }
//            }
//            State.ERROR -> {
//                with( {
//                    downloadableContent.visibility = View.GONE
//                    errorContainer.visibility = View.VISIBLE
//                    progressBar.visibility = View.GONE
//                }
//            }
//            State.LOADING -> {
//                with( {
//                    downloadableContent.visibility = View.GONE
//                    errorContainer.visibility = View.GONE
//                    progressBar.visibility = View.VISIBLE
//                }
//            }
//        }
    }

    override fun onBackPressed() {
        if (servicesBlock.visibility == View.VISIBLE) {
            showServicesBlock(false)
            return
        }
        super.onBackPressed()
    }


    private fun showServicesBlock(show: Boolean) {
        val animation: Animation
        if (show) {
            animation = AnimationUtils.loadAnimation(this, R.anim.bottom_up)
            servicesBlock.visibility = View.VISIBLE
        } else {
            animation = AnimationUtils.loadAnimation(this, R.anim.bottom_down)
            servicesBlock.visibility = View.GONE
        }
        servicesBlock.startAnimation(animation)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}