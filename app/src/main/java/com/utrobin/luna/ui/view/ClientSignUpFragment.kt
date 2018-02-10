package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.utrobin.luna.R
import com.utrobin.luna.databinding.ClientSignUpFragmentBinding
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.ClientSignUpContract
import com.utrobin.luna.ui.presenter.ClientSignUpPresenter

/**
 * Created by ivan on 08.02.2018.
 */

class ClientSignUpFragment : Fragment(), ClientSignUpContract.View {
    lateinit var binding: ClientSignUpFragmentBinding

    private val presenter = ClientSignUpPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.client_sign_up_fragment, container, false)
        binding.signUpBtn.setOnClickListener { presenter.onSignUpButtonClicked() }
        return binding.root
    }

    override fun validateFields(): Boolean {
        if (!presenter.isLoginCorrect(binding.username.text.toString())) {
            binding.username.error = getString(R.string.wrong_format)
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
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.container.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun getUsername() = binding.username.text.toString()

    override fun getName() = binding.name.text.toString()

    override fun getEmail() = binding.email.text.toString()

    override fun getPassword() = binding.password.text.toString()

    override fun signUpFinished() {
        Toast.makeText(context, R.string.sign_up_successful, Toast.LENGTH_SHORT).show()
    }

    override fun signUpFailed(reason: NetworkError) {
        Toast.makeText(context, R.string.sign_up_failed, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        fun getInstance() = ClientSignUpFragment()
    }
}