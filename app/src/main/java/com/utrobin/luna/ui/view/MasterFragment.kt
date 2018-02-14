package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter
import com.utrobin.luna.databinding.MasterFragmentBinding
import com.utrobin.luna.model.MasterBase
import com.utrobin.luna.model.MasterExtended
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.MasterContract
import com.utrobin.luna.ui.presenter.MasterPresenter
import kotlinx.android.synthetic.main.error_container.view.*

/**
 * Created by ivan on 01.11.2017.
 */
class MasterFragment : Fragment(), MasterContract.View {
    private lateinit var master: MasterExtended

    lateinit var binding: MasterFragmentBinding

    private val presenter = MasterPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move);
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.master_fragment, container, false)!!
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setState(State.LOADING)

        val base = arguments?.getParcelable<MasterBase>(MASTER_BASE)
                ?: throw NullPointerException("No arguments provided!")

        binding.toolbar.title = base.name
        presenter.loadData(base)
        binding.errorContainer!!.repeat_btn.setOnClickListener {
            setState(State.LOADING)
            presenter.loadData(base)
        }

        binding.pager.adapter = ViewPagerAdapter(context!!, base.photos)


        // Animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.pager.transitionName = arguments!!.getString(TRANSITION_NAME)
        }
        startPostponedEnterTransition();
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
        binding.rating.text = context!!.resources.getQuantityString(R.plurals.ratings_count,
                reviewsCount, master.base.stars.toString(), reviewsCount)
        drawStars()

        binding.description.text = "Мы легко впишемся в ваш график, а все наши услуги" +
                " не займут у вас много времени."
    }

    private fun drawStars() {
        val fullStarsCount = master.base.stars.toInt()
        val halfStarPresented = master.base.stars - fullStarsCount >= 0.5

        val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        params.setMargins(0, 0, context!!.resources.getDimension(R.dimen.master_space_between_stars).toInt(), 0)

        for (i in 0 until fullStarsCount) {
            val fullStar = ImageView(context!!)
            fullStar.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_star_black_24dp))
            fullStar.layoutParams = params
            binding.starsContainer.addView(fullStar)
        }

        if (halfStarPresented) {
            val halfStar = ImageView(context!!)
            halfStar.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_star_half_black_24dp))
            halfStar.layoutParams = params
            binding.starsContainer.addView(halfStar)
        }


        for (i in 4 downTo binding.starsContainer.childCount) {
            val emptyStar = ImageView(context!!)
            emptyStar.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_star_border_black_24dp))
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


    companion object {
        fun getInstance(masterBase: MasterBase, transitionName: String): MasterFragment {
            val bundle = Bundle()
            bundle.putParcelable(MASTER_BASE, masterBase)
            bundle.putString(TRANSITION_NAME, transitionName)
            val fragment = MasterFragment()
            fragment.arguments = bundle
            return fragment
        }

        private const val MASTER_BASE = "MASTER_BASE_EXTRA"
        private const val TRANSITION_NAME = "TRANSITION_NAME"
    }

    private enum class State {
        CONTENT, ERROR, LOADING
    }
}