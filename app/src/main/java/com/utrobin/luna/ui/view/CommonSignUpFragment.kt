package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utrobin.luna.R
import com.utrobin.luna.databinding.CommonSignUpFragmentBinding
import com.utrobin.luna.ui.contract.CommonSignUpContact
import com.utrobin.luna.ui.presenter.CommonSignUpPresenter

/**
 * Created by ivan on 05.11.2017.
 */

class CommonSignUpFragment : Fragment(), CommonSignUpContact.View {
    lateinit var binding: CommonSignUpFragmentBinding

    private val presenter = CommonSignUpPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.common_sign_up_fragment, container, false)
        binding.signUpAsUserBtn.setOnClickListener { presenter.signUpAsUserSelected() }
        binding.signUpAsMasterBtn.setOnClickListener { presenter.signUpAsMasterSelected() }
        return binding.root
    }

    override fun showSignUpAsUserFragment() {
        activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container, ClientSignUpFragment.getInstance())
                ?.addToBackStack(null)
                ?.commitAllowingStateLoss()
    }

    override fun showSignUpAsMasterFragment() {
        activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container, MasterSignUpFragment.getInstance())
                ?.addToBackStack(null)
                ?.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        fun getInstance() = CommonSignUpFragment()
    }
}