package com.utrobin.luna.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.utrobin.luna.R
import com.utrobin.luna.utils.MapControllerWrapper
import ru.yandex.yandexmapkit.MapView


/**
 * Created by ivan on 04.11.2017.
 */

class MapFragment : Fragment() {
    @BindView(R.id.map)
    lateinit var mapView: MapView

    private lateinit var mapController: MapControllerWrapper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.map_fragment, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapController = MapControllerWrapper(mapView)
        mapController.overlayManager.myLocation.isEnabled = true

    }
}