package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utrobin.luna.R
import com.utrobin.luna.databinding.MapFragmentBinding
import com.utrobin.luna.utils.MapControllerWrapper


/**
 * Created by ivan on 04.11.2017.
 */

class MapFragment : Fragment() {

    lateinit var binding: MapFragmentBinding

    private lateinit var mapController: MapControllerWrapper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.map_fragment, container, false)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapController = MapControllerWrapper(binding.map)
        mapController.overlayManager.myLocation.isEnabled = true

    }
}