package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utrobin.luna.R
import com.utrobin.luna.databinding.MasterSignUpFragmentBinding
import com.utrobin.luna.ui.contract.MasterSignUpContract
import com.utrobin.luna.ui.presenter.MasterSignUpPresenter

/**
 * Created by ivan on 08.02.2018.
 */


class MasterSignUpFragment : Fragment(), MasterSignUpContract.View {
    lateinit var binding: MasterSignUpFragmentBinding

    private val presenter = MasterSignUpPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.master_sign_up_fragment, container, false)
        binding.signUpBtn.setOnClickListener { presenter.onSignUpButtonClicked() }
        return binding.root
    }

    override fun validateFields(): Boolean {
        if (!presenter.isLoginCorrect(binding.login.text.toString())) {
            binding.login.error = getString(R.string.wrong_format)
            return false
        }

        if (!presenter.isNameCorrect(binding.name.text.toString())) {
            binding.name.error = getString(R.string.wrong_format)
            return false
        }

        if (!presenter.isEmailCorrect(binding.email.text.toString())) {
            binding.email.error = getString(R.string.wrong_format)
            return false
        }

        if (!presenter.isPasswordCorrect(binding.password.text.toString())) {
            binding.password.error = getString(R.string.wrong_format)
            return false
        }

        if (binding.password.text.toString() != binding.repeatPassword.text.toString()) {
            binding.password.error = getString(R.string.passwords_dont_match)
            return false
        }
        return true
    }

    override fun showProgressBar(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorMsg(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSuccessfulMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        fun getInstance() = MasterSignUpFragment()
    }
}