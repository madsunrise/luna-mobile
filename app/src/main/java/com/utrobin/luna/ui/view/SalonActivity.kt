package com.utrobin.luna.ui.view

import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter
import com.utrobin.luna.entity.FeedItem
import com.utrobin.luna.entity.Salon
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.SalonContract
import com.utrobin.luna.ui.presenter.SalonPresenter
import com.utrobin.luna.utils.Utils.fillContainerWithStars
import kotlinx.android.synthetic.main.salon_activity.*
import java.util.*
import java.util.concurrent.TimeUnit


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

        supportPostponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageSlider.transitionName = intent.extras.getString(TRANSITION_NAME)
            imageSlider.currentItem = intent.extras.getInt(CURRENT_PHOTO)
        }
        supportStartPostponedEnterTransition()

        fillBaseViews(base)
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
        fillViewsAfterNetworkRequest()
    }

    override fun onDataLoadingFailed(reason: NetworkError) {
        setState(State.ERROR)
    }

    private fun fillBaseViews(base: FeedItem) {
        setUpImageSlider(base)
        fillRatingWithName(base)
        fillAddress(base)

        buttonWhat.setOnClickListener { showServicesBlock(true) }
        closeServicesBtn.setOnClickListener { showServicesBlock(false) }
    }

    private fun fillViewsAfterNetworkRequest() {
        fillSigns()
        fillReviews()
        fillStubs()
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


    private fun setUpImageSlider(base: FeedItem) {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        imageSlider.layoutParams.height = displayMetrics.widthPixels * 9 / 16

        val adapter = ViewPagerAdapter(this, base.photos)
        imageSlider.adapter = adapter
        adapter.imageClickSubject.subscribe {
            //openImageViewer()
        }

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


    private fun fillRatingWithName(base: FeedItem) {
        findViewById<TextView>(R.id.title).text = base.name ?: throw IllegalArgumentException("Salon's name is not presented")
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 0, resources.getDimension(R.dimen.salon_activity_space_between_stars).toInt(), 0)
        fillContainerWithStars(this, base.stars, starsContainer, params)
        ratesCount.text = resources.getQuantityString(R.plurals.rates_count, base.ratesCount, base.ratesCount)
    }

    private fun fillSigns() {
        if (salon.signs.isEmpty()) {
            signsContainer.visibility = View.GONE
            return
        }

        for (i in 0 until salon.signs.size) {
            val sign = salon.signs[i]
            val signLayout = LayoutInflater
                    .from(this)
                    .inflate(R.layout.salon_activity_sign, signsContainer, false)

            if (i == 0) {
                signLayout.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(
                            resources.getDimension(R.dimen.activity_horizontal_margin).toInt(),
                            0,
                            resources.getDimension(R.dimen.salon_activity_space_between_signs).toInt(),
                            0
                    )
                }
            } else if (i == salon.signs.size - 1) {
                signLayout.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(
                            0,
                            0,
                            resources.getDimension(R.dimen.activity_horizontal_margin).toInt(),
                            0
                    )
                }
            } else {
                signLayout.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(
                            0,
                            0,
                            resources.getDimension(R.dimen.salon_activity_space_between_signs).toInt(),
                            0
                    )
                }
            }


            val signImage = signLayout.findViewById<ImageView>(R.id.signImage)
            //Glide.with(this).load(sign.icon).into(signImage)
            Glide.with(this).load("https://thumbs.dreamstime.com/b/%D0%B7%D0%BD%D0%B0%D1%87%D0%BE%D0%BA-%D0%B2%D0%B5%D0%BA%D1%82%D0%BE%D1%80%D0%B0-89527209.jpg").into(signImage)

            signLayout.findViewById<TextView>(R.id.signName).text = sign.name

            val signCount = salon.signsTotal[sign.id]
                    ?: throw IllegalArgumentException("Unable to find sign count")

            val ratesCountView = signLayout.findViewById<TextView>(R.id.ratesCount)
            ratesCountView.text = resources.getQuantityString(R.plurals.rates_count, signCount, signCount)

            signsContainer.addView(signLayout)
        }
    }

    private fun fillAddress(base: FeedItem) {
        if (base.address == null) {
            sendEvent("No address presented in salon ${base.id}")
            addressContainer.visibility = View.GONE
            return
        }
        if (base.address.metro.isEmpty()) {
            sendEvent("No metro provided for salon ${base.id}")
            addressContainer.visibility = View.GONE
            return
        }

        addressMetro.text = base.address.metro[0].station
        // TODO dot
        addressDescription.text = base.address.description
    }


    private fun fillReviews() {
        if (salon.lastReviews.isEmpty()) {
            reviewsContainer.visibility = View.GONE
            seeAllReviews.visibility = View.GONE
            return
        }


        val reviewsCount = salon.lastReviews.size
        for (i in 0 until reviewsCount) {
            val reviewLayout = LayoutInflater
                    .from(this)
                    .inflate(R.layout.review, reviewsContainerInternal, false)

            if (i == 0) {
                reviewLayout.layoutParams = LinearLayout.LayoutParams(
                        resources.getDimension(R.dimen.salon_activity_review_width).toInt(),
                        resources.getDimension(R.dimen.salon_activity_review_height).toInt()
                ).apply {
                    setMargins(
                            resources.getDimension(R.dimen.activity_horizontal_margin).toInt(),
                            0,
                            resources.getDimension(R.dimen.salon_activity_space_between_reviews).toInt(),
                            0
                    )
                }
            } else if (i == reviewsCount - 1) {
                reviewLayout.layoutParams = LinearLayout.LayoutParams(
                        resources.getDimension(R.dimen.salon_activity_review_width).toInt(),
                        resources.getDimension(R.dimen.salon_activity_review_height).toInt()
                ).apply {
                    setMargins(
                            0,
                            0,
                            resources.getDimension(R.dimen.activity_horizontal_margin).toInt(),
                            0
                    )
                }
            } else {
                reviewLayout.layoutParams = LinearLayout.LayoutParams(
                        resources.getDimension(R.dimen.salon_activity_review_width).toInt(),
                        resources.getDimension(R.dimen.salon_activity_review_height).toInt()
                ).apply {
                    setMargins(
                            0,
                            0,
                            resources.getDimension(R.dimen.salon_activity_space_between_reviews).toInt(),
                            0
                    )
                }
            }

            val review = salon.lastReviews[i]

            val author = review.client.name
                    ?: throw IllegalArgumentException("Review's author is null")
            reviewLayout.findViewById<TextView>(R.id.reviewAuthor).text = author

            // TODO add master name here
            reviewLayout.findViewById<TextView>(R.id.reviewDate).text = parseReviewDate(review.date)
            reviewLayout.findViewById<TextView>(R.id.reviewText).text = review.message
                    ?: throw IllegalArgumentException("Review's message is null")

            reviewsContainerInternal.addView(reviewLayout)
        }

        if (salon.commentsCount == reviewsCount) {
            seeAllReviews.visibility = View.GONE
        } else {
            seeAllReviews.text = resources.getQuantityString(
                    R.plurals.see_all_n_reviews, salon.commentsCount, salon.commentsCount
            )
        }
    }

    private fun parseReviewDate(date: Date): String {
        val days = TimeUnit.DAYS.convert(Date().time - date.time, TimeUnit.MILLISECONDS)
        return resources.getQuantityString(R.plurals.review_date, days.toInt(), days.toInt())
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

    private fun sendEvent(msg: String) {
        Answers.getInstance().logCustom(
                CustomEvent("Salon Activity")
                        .putCustomAttribute("msg", msg)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun fillStubs() {
        buttonWhen.text = "12 марта, 19:15"
        buttonWhat.text = "3 услуги"
        fillSuitableMasters()
        fillMakeAnAppointmentStub()
    }

    private fun fillSuitableMasters() {

        val mastersCount = salon.masters.size


        mastersSuitableForYou.text = resources.getQuantityString(
                R.plurals.n_masters_suitable_for_you, mastersCount, mastersCount)

        for (i in 0 until mastersCount) {

            val master = salon.masters[i]

            val masterView = LayoutInflater
                    .from(this)
                    .inflate(R.layout.suitable_master, suitableMastersContainer, false)

            // TODO we need certificate
            Glide.with(this)
                    .load("https://cdn.pixabay.com/photo/2017/01/03/09/18/woman-1948939_960_720.jpg")
                    .apply(RequestOptions.circleCropTransform())
                    .into(masterView.findViewById(R.id.avatar))

//            if (master.avatar != null) {
//                Glide.with(this)
//                        .load(master.avatar)
//                        .apply(RequestOptions.circleCropTransform())
//                        .into(masterView.findViewById(R.id.avatar))
//            } else {
//                Glide.with(this)
//                        .load(R.drawable.no_avatar)
//                        .apply(RequestOptions.circleCropTransform())
//                        .into(masterView.findViewById(R.id.avatar))
//            }

            val roles = arrayOf("Мастер-стилист", "Ведущий стилист", "Арт-директор")

            // TODO too long name will overlap price
            masterView.findViewById<TextView>(R.id.name).text = master.name ?: throw IllegalArgumentException("Master's name is null")
            masterView.findViewById<TextView>(R.id.role).text = roles.get(Random().nextInt(3))
            masterView.findViewById<TextView>(R.id.price).text = "2500 \u20BD"

            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, resources.getDimension(R.dimen.suitable_master_space_between_stars).toInt(), 0)
            fillContainerWithStars(this, master.stars, masterView.findViewById<ViewGroup>(R.id.starsContainer), params)
            masterView.findViewById<TextView>(R.id.ratesCount).text = resources.getQuantityString(R.plurals.rates_count, master.ratesCount, master.ratesCount)

            suitableMastersContainer.addView(masterView)
        }
    }

    private fun fillMakeAnAppointmentStub() {
        appointmentPrice.text = "2500 \u20BD"
        appointmentDuration.text = "1ч 20м"
        appointmentMasterDescription.text = "Алия Агиповна, мастер-стилист"
    }
}