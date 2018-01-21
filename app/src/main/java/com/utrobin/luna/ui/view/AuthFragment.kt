package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utrobin.luna.R
import com.utrobin.luna.databinding.AuthFragmentBinding
import com.utrobin.luna.ui.contract.AuthContract
import com.utrobin.luna.ui.presenter.AuthPresenter

/**
 * Created by ivan on 05.11.2017.
 */

class AuthFragment : Fragment(), AuthContract.View {
    lateinit var binding: AuthFragmentBinding

    private val presenter = AuthPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment, container, false)!!
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        fun getInstance() = AuthFragment()
    }
}