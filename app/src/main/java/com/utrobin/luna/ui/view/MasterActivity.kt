package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter
import com.utrobin.luna.databinding.MasterActivityBinding
import com.utrobin.luna.model.MasterBase
import com.utrobin.luna.model.MasterExtended
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.MasterContract
import com.utrobin.luna.ui.presenter.MasterPresenter
import kotlinx.android.synthetic.main.error_container.view.*


/**
 * Created by ivan on 01.11.2017.
 */
class MasterActivity : AppCompatActivity(), MasterContract.View {
    private lateinit var master: MasterExtended

    lateinit var binding: MasterActivityBinding

    private val presenter = MasterPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.master_activity)

        presenter.attachView(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val base = intent.extras.getParcelable<MasterBase>(MASTER_BASE)

        binding.toolbar.title = base.name
        setState(State.LOADING)

        presenter.loadData(base)
        binding.errorContainer!!.repeat_btn.setOnClickListener {
            setState(State.LOADING)
            presenter.loadData(base)
        }

        binding.pager.adapter = ViewPagerAdapter(this, base.photos)


        supportPostponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.pager.transitionName = intent.extras.getString(TRANSITION_NAME)
            binding.pager.currentItem = intent.extras.getInt(CURRENT_PHOTO)
        }
        supportStartPostponedEnterTransition();
    }

    override fun dataLoaded(master: MasterExtended) {
        setState(State.CONTENT)
        this.master = master

        fillViews()
    }

    override fun dataLoadingFailed(reason: NetworkError) {
        setState(State.ERROR)
    }

    private fun fillViews() {
        binding.title.text = master.base.name

        val reviewsCount = 124
        binding.rating.text = resources.getQuantityString(R.plurals.ratings_count,
                reviewsCount, master.base.stars.toString(), reviewsCount)
        drawStars()

        binding.description.text = "Мы легко впишемся в ваш график, а все наши услуги" +
                " не займут у вас много времени."

        binding.addressDescription.text = master.base.address.description
    }

    private fun drawStars() {
        val fullStarsCount = master.base.stars.toInt()
        val halfStarPresented = master.base.stars - fullStarsCount >= 0.5

        val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        params.setMargins(0, 0, resources.getDimension(R.dimen.master_space_between_stars).toInt(), 0)

        for (i in 0 until fullStarsCount) {
            val fullStar = ImageView(this)
            fullStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp))
            fullStar.layoutParams = params
            binding.starsContainer.addView(fullStar)
        }

        if (halfStarPresented) {
            val halfStar = ImageView(this)
            halfStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_half_black_24dp))
            halfStar.layoutParams = params
            binding.starsContainer.addView(halfStar)
        }


        for (i in 4 downTo binding.starsContainer.childCount) {
            val emptyStar = ImageView(this)
            emptyStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp))
            emptyStar.layoutParams = params
            binding.starsContainer.addView(emptyStar)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun setState(state: State) {
        when (state) {
            State.CONTENT -> {
                with(binding) {
                    mainContainer.visibility = View.VISIBLE
                    errorContainer!!.visibility = View.GONE
                    progressContainer!!.visibility = View.GONE
                }
            }
            State.ERROR -> {
                with(binding) {
                    mainContainer.visibility = View.GONE
                    errorContainer!!.visibility = View.VISIBLE
                    progressContainer!!.visibility = View.GONE
                }
            }
            State.LOADING -> {
                with(binding) {
                    //mainContainer.visibility = View.GONE
                    errorContainer!!.visibility = View.GONE
                    //progressContainer!!.visibility = View.VISIBLE
                }
            }
        }
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

    companion object {
        const val MASTER_BASE = "MASTER_BASE_EXTRA"
        const val TRANSITION_NAME = "TRANSITION_NAME_EXTRA"
        const val CURRENT_PHOTO = "CURRENT_PHOTO_EXTRA"
    }

    private enum class State {
        CONTENT, ERROR, LOADING
    }
}