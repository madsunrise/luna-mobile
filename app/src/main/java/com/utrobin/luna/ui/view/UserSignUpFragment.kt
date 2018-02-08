package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utrobin.luna.R
import com.utrobin.luna.databinding.UserSignUpFragmentBinding
import com.utrobin.luna.ui.contract.UserSignUpContract
import com.utrobin.luna.ui.presenter.UserSignUpPresenter

/**
 * Created by ivan on 08.02.2018.
 */

class UserSignUpFragment : Fragment(), UserSignUpContract.View {
    lateinit var binding: UserSignUpFragmentBinding

    private val presenter = UserSignUpPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_sign_up_fragment, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        fun getInstance() = UserSignUpFragment()
    }
}