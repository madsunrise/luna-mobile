package com.utrobin.luna.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.utrobin.luna.R
import com.utrobin.luna.ui.contract.MasterContract
import com.utrobin.luna.utils.MapControllerWrapper
import de.hdodenhof.circleimageview.CircleImageView
import ru.yandex.yandexmapkit.MapView
import ru.yandex.yandexmapkit.overlay.Overlay
import ru.yandex.yandexmapkit.overlay.OverlayItem
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem
import ru.yandex.yandexmapkit.utils.GeoPoint
import java.util.*

/**
 * Created by ivan on 01.11.2017.
 */
class MasterFragment : Fragment(), MasterContract.View {
    private lateinit var mMapController: MapControllerWrapper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.master_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load("http://wordprint.ru/attachments/Image/1.png?template=generic").into((view.findViewById(R.id.toolbar_image)))
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Jasmine"
        Glide.with(this).load("http://100idey.ru/wp-content/uploads/2017/01/manikur6.jpg").into((view.findViewById(R.id.image)))

        val cloud = ImageView(context)
        cloud.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cloud_black_24dp))
        val achievementSize = resources.getDimension(R.dimen.achievement_size).toInt()
        val params = LinearLayout.LayoutParams(achievementSize, achievementSize)
        params.setMargins(0, 0, resources.getDimension(R.dimen.achievement_margin_right).toInt(), 0)
        cloud.layoutParams = params
        view.findViewById<LinearLayout>(R.id.achievements_container).addView(cloud)

        val chat = ImageView(context)
        chat.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_chat_black_24dp))
        chat.layoutParams = params
        view.findViewById<LinearLayout>(R.id.achievements_container).addView(chat)

        val notification = ImageView(context)
        notification.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_notifications_black_24dp))
        notification.layoutParams = params
        view.findViewById<LinearLayout>(R.id.achievements_container).addView(notification)

        view.findViewById<TextView>(R.id.first_opinion_tv).setText("Vika: Была у Екатерины, хорошо сделала шеллак.")
        view.findViewById<TextView>(R.id.see_all_opinions_tv).setText("Посмотреть все комментарии (42)")
        view.findViewById<TextView>(R.id.initial_cost_tv).setText("Начальная стоимость от 400р")


        view.findViewById<TextView>(R.id.address_tv).setText("Москва, Ленинградский проспект, 39c9")

        val mapView = view.findViewById<MapView>(R.id.map)
        mMapController = MapControllerWrapper(mapView)
        mMapController.overlayManager.myLocation.isEnabled = false
        showObject()

        addWorkers(view)
    }

    fun showObject() {
        // Create a layer of objects for the map
        val overlay = Overlay(mMapController.mapController)

        val point = GeoPoint(55.79694821, 37.53778351)
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_place_black_24dp)
        // Create an object for the layer
        val yandex = OverlayItem(point, icon)
        // Create the balloon model for the object
        val balloonYandex = BalloonItem(context, yandex.geoPoint)
        balloonYandex.text = "Mail.ru Group"
        // balloonYandex.setOnBalloonListener(context)
        // Add the balloon model to the object
        yandex.balloonItem = balloonYandex
        // Add the object to the layer
        overlay.addOverlayItem(yandex)
        mMapController.setPositionAnimationTo(point, MapControllerWrapper.OPTIMAL_ZOOM)

        // Add the layer to the map
        mMapController.overlayManager.addOverlay(overlay)

    }

    private fun addWorkers(view: View) {
        val container = view.findViewById<LinearLayout>(R.id.workers_container)
        val spaceBetween = resources.getDimension(R.dimen.master_space_between_workers).toInt();

        val size = resources.getDimension(R.dimen.master_worker_photo_size).toInt()
        val params = LinearLayout.LayoutParams(size, size)
        params.setMargins(spaceBetween, 0, spaceBetween, 0)

        val first = CircleImageView(context)
        first.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.all))
        first.layoutParams = params
        first.borderColor = ContextCompat.getColor(context, R.color.black)
        first.borderWidth = resources.getDimension(R.dimen.master_worker_border_width).toInt()


        val padding = resources.getDimension(R.dimen.master_workers_container_padding_vertical).toInt()
        container.setPadding(0, padding, 0, padding)
        container.addView(first)
        for (i in 0..10) {
            val worker = LinearLayout(context)
            val wrapperParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            wrapperParams.setMargins(0, 0, spaceBetween, 0)
            worker.layoutParams = wrapperParams
            worker.orientation = LinearLayout.VERTICAL


            val workerImage = CircleImageView(context)
            workerImage.layoutParams = LinearLayout.LayoutParams(size, size)

            workerImage.borderColor = ContextCompat.getColor(context, R.color.black)
            workerImage.borderWidth = resources.getDimension(R.dimen.master_worker_border_width).toInt()
            Glide
                    .with(context)
                    .load(photoUrls[i % photoUrls.size])
                    .into(workerImage)
            worker.addView(workerImage)

            val workerName = TextView(context)
            workerName.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_size_caption))
            workerName.text = names[Random().nextInt(names.size)]
            workerName.setTextColor(ContextCompat.getColor(context, R.color.black))
            workerName.gravity = Gravity.CENTER
            worker.addView(workerName)

            container.addView(worker)
        }
    }


    val photoUrls = arrayOf(
            "https://photov3zoosk-a.akamaihd.net/00253397573040192041/s320.jpg",
            "https://photov3zoosk-a.akamaihd.net/00302298528762382963/s320.jpg",
            "https://i.pinimg.com/736x/77/74/e9/7774e98f623abe1269b4a09bba5e15a8--common-core-sexy-women.jpg",
            "https://runwaygirl-5389.kxcdn.com/wp-content/uploads/2017/05/IMG_7251-256x256.jpg",
            "https://pbs.twimg.com/profile_images/2804122722/142d1f48a8e3f2cf47ea46aee887dd88.jpeg",
            "https://images-na.ssl-images-amazon.com/images/I/71kqo6Lw6ML._SL256_.jpg",
            "https://i.pinimg.com/originals/d5/56/9b/d5569b8a972bc6bfffd99cd136e669d9.jpg",
            "https://pbs.twimg.com/profile_images/378800000826166880/5e7abbd9cfc67d93512e40e68867005b.jpeg",
            "https://pbs.twimg.com/profile_images/2910090330/ac75e470c41b75e3c3915cd6aed573c1.jpeg",
            "https://pbs.twimg.com/profile_images/3071608751/a316fb017770f0400713bda9e9ee4b39.jpeg"
    )

    val names = arrayOf(
            "Ксения",
            "Екатерина",
            "Елизавета",
            "Арина",
            "Анна",
            "Алёна",
            "Елена",
            "Татьяна",
//            "Александра"
            "Светлана",
            "Яна",
            "Оксана",
            "Василиса",
            "Инна",
            "Алла",
            "Алевтина",
            "Дарья",
            "Виктория",
            "Надежда",
            "Анастасия",
            "Юлия",
            "Кристина",
            "Ольга"
    )
}