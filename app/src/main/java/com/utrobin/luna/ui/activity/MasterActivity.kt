package com.utrobin.luna.ui.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.utrobin.luna.R
import com.utrobin.luna.utils.MapControllerWrapper
import ru.yandex.yandexmapkit.MapView
import ru.yandex.yandexmapkit.overlay.Overlay
import ru.yandex.yandexmapkit.overlay.OverlayItem
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem
import ru.yandex.yandexmapkit.utils.GeoPoint

/**
 * Created by ivan on 01.11.2017.
 */
class MasterActivity : AppCompatActivity() {
    private lateinit var mMapController: MapControllerWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.master_activity)
        Glide.with(this).load("http://wordprint.ru/attachments/Image/1.png?template=generic").into((findViewById(R.id.toolbar_image)))
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Paradise"
        Glide.with(this).load("http://100idey.ru/wp-content/uploads/2017/01/manikur6.jpg").into((findViewById(R.id.image)))

        val cloud = ImageView(this)
        cloud.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cloud_black_24dp))
        val achievementSize = resources.getDimension(R.dimen.achievement_size).toInt()
        val params = LinearLayout.LayoutParams(achievementSize, achievementSize)
        params.setMargins(0, 0, resources.getDimension(R.dimen.achievement_margin_right).toInt(), 0)
        cloud.layoutParams = params
        findViewById<LinearLayout>(R.id.achievements_container).addView(cloud)

        val chat = ImageView(this)
        chat.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_chat_black_24dp))
        chat.layoutParams = params
        findViewById<LinearLayout>(R.id.achievements_container).addView(chat)

        val notification = ImageView(this)
        notification.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_notifications_black_24dp))
        notification.layoutParams = params
        findViewById<LinearLayout>(R.id.achievements_container).addView(notification)

        findViewById<TextView>(R.id.first_opinion_tv).setText("Vika: Была у Екатерины, хорошо сделала шеллак.")
        findViewById<TextView>(R.id.see_all_opinions_tv).setText("Посмотреть все комментарии (42)")
        findViewById<TextView>(R.id.initial_cost_tv).setText("Начальная стоимость от 400р")

        val mapView = findViewById<MapView>(R.id.map)

        mMapController = MapControllerWrapper(mapView)


        mMapController.overlayManager.myLocation.isEnabled = false
        showObject()
    }

    fun showObject() {
        // Create a layer of objects for the map
        val overlay = Overlay(mMapController.mapController)


        val icon = ContextCompat.getDrawable(this, R.drawable.ic_place_black_24dp)
        // Create an object for the layer
        val yandex = OverlayItem(GeoPoint(55.734029, 37.588499), icon)
        // Create the balloon model for the object
        val balloonYandex = BalloonItem(this, yandex.geoPoint)
        balloonYandex.text = "Yandex"
        // balloonYandex.setOnBalloonListener(this)
        // Add the balloon model to the object
        yandex.balloonItem = balloonYandex
        // Add the object to the layer
        overlay.addOverlayItem(yandex)

        // Add the layer to the map
        mMapController.overlayManager.addOverlay(overlay)

    }

}