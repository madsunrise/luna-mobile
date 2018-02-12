package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter
import com.utrobin.luna.databinding.MasterFragmentBinding
import com.utrobin.luna.model.Master
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.MasterContract
import com.utrobin.luna.ui.presenter.MasterPresenter
import kotlinx.android.synthetic.main.error_container.view.*

/**
 * Created by ivan on 01.11.2017.
 */
class MasterFragment : Fragment(), MasterContract.View {
    private lateinit var master: Master

    lateinit var binding: MasterFragmentBinding

    private val presenter = MasterPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
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
        this.master = arguments?.getParcelable(MASTER_BASE)
                ?: throw NullPointerException("No arguments provided!")

        binding.toolbar.title = master.name
        presenter.loadData(master.id)
        binding.errorContainer!!.repeat_btn.setOnClickListener {
            setState(State.LOADING)
            presenter.loadData(master.id)
        }

        binding.pager.adapter = ViewPagerAdapter(context!!, master.photos)
    }

    override fun dataLoaded(master: Master) {
        setState(State.CONTENT)
        // TODO something!
        //this.master = master
    }

    override fun dataLoadingFailed(reason: NetworkError) {
        setState(State.ERROR)
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
        fun getInstance(master: Master): MasterFragment {
            val bundle = Bundle()
            bundle.putParcelable(MASTER_BASE, master)
            val fragment = MasterFragment()
            fragment.arguments = bundle
            return fragment
        }

        private const val MASTER_BASE = "MASTER_BASE_EXTRA"
    }

    private enum class State {
        CONTENT, ERROR, LOADING
    }
}