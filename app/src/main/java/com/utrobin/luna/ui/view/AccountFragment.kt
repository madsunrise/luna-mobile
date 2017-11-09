package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utrobin.luna.R
import com.utrobin.luna.databinding.AccountFragmentBinding

/**
 * Created by ivan on 05.11.2017.
 */

class AccountFragment : Fragment() {
    lateinit var binding: AccountFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.account_fragment, container, false);
        return binding.root
    }
}