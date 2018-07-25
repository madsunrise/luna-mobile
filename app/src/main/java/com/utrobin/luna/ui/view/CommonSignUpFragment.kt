package com.utrobin.luna.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utrobin.luna.R
import com.utrobin.luna.ui.contract.CommonSignUpContact
import com.utrobin.luna.ui.presenter.CommonSignUpPresenter
import kotlinx.android.synthetic.main.common_sign_up_fragment.*

class CommonSignUpFragment : Fragment(), CommonSignUpContact.View {
    private val presenter = CommonSignUpPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.common_sign_up_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpAsUserBtn.setOnClickListener { presenter.signUpAsUserSelected() }
        signUpAsMasterBtn.setOnClickListener { presenter.signUpAsMasterSelected() }
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