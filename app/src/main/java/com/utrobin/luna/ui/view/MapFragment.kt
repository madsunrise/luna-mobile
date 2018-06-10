package com.utrobin.luna.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utrobin.luna.R
import com.utrobin.luna.utils.MapControllerWrapper
import kotlinx.android.synthetic.main.map_fragment.*


/**
 * Created by ivan on 04.11.2017.
 */

class MapFragment : Fragment() {

    private lateinit var mapController: MapControllerWrapper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapController = MapControllerWrapper(map)
        mapController.overlayManager.myLocation.isEnabled = true

    }

    companion object {
        fun getInstance() = MapFragment()
    }
}