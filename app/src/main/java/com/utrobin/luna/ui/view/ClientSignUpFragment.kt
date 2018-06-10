package com.utrobin.luna.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.utrobin.luna.R
import com.utrobin.luna.network.NetworkError
import com.utrobin.luna.ui.contract.ClientSignUpContract
import com.utrobin.luna.ui.presenter.ClientSignUpPresenter
import kotlinx.android.synthetic.main.client_sign_up_fragment.*

/**
 * Created by ivan on 08.02.2018.
 */

class ClientSignUpFragment : Fragment(), ClientSignUpContract.View {

    private val presenter = ClientSignUpPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.client_sign_up_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpBtn.setOnClickListener { presenter.onSignUpButtonClicked() }
    }

    override fun validateFields(): Boolean {
        if (!presenter.isLoginCorrect(username.text.toString())) {
            username.error = getString(R.string.wrong_format)
            return false
        }

        if (!presenter.isNameCorrect(name.text.toString())) {
            name.error = getString(R.string.wrong_format)
            return false
        }

        if (!presenter.isEmailCorrect(email.text.toString())) {
            email.error = getString(R.string.wrong_format)
            return false
        }

        if (!presenter.isPasswordCorrect(password.text.toString())) {
            password.error = getString(R.string.wrong_format)
            return false
        }

        if (password.text.toString() != repeatPassword.text.toString()) {
            password.error = getString(R.string.passwords_dont_match)
            return false
        }
        return true
    }

    override fun showProgressBar(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        container.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun getUsername() = username.text.toString()

    override fun getName() = name.text.toString()

    override fun getEmail() = email.text.toString()

    override fun getPassword() = password.text.toString()

    override fun signUpFinished() {
        Toast.makeText(context, R.string.sign_up_successful, Toast.LENGTH_SHORT).show()
        activity
                ?.supportFragmentManager
                ?.popBackStack()
    }

    override fun signUpFailed(reason: NetworkError) {
        Toast.makeText(context, R.string.sign_up_failed, Toast.LENGTH_SHORT).show()
        activity
                ?.supportFragmentManager
                ?.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        fun getInstance() = ClientSignUpFragment()
    }
}