package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
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
import com.utrobin.luna.adapter.ViewPagerAdapter
import com.utrobin.luna.adapter.ViewPagerAdapter.Companion.addBottomDots
import com.utrobin.luna.databinding.MasterFragmentBinding
import com.utrobin.luna.model.FeedItem
import com.utrobin.luna.ui.contract.MasterContract
import com.utrobin.luna.utils.MapControllerWrapper
import com.utrobin.luna.utils.svg.SvgModule
import de.hdodenhof.circleimageview.CircleImageView
import ru.yandex.yandexmapkit.overlay.Overlay
import ru.yandex.yandexmapkit.overlay.OverlayItem
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem
import ru.yandex.yandexmapkit.utils.GeoPoint
import java.util.*

/**
 * Created by ivan on 01.11.2017.
 */
class MasterFragment : Fragment(), MasterContract.View {
    private lateinit var feedItem: FeedItem

    lateinit var binding: MasterFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feedItem = arguments?.getParcelable(FEED_ITEM_KEY) ?: throw NullPointerException("No arguments provided!")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.master_fragment, container, false)!!
        binding.toolbar.title = feedItem.name

        setupMap()
        setupWorkers()
        setupSigns()
        setupOther()
        setupViewPager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showProgressBar(false)
    }


    private fun setupMap() {
        val mapController = MapControllerWrapper(binding.map)
        mapController.overlayManager.myLocation.isEnabled = false


        // Create a layer of objects for the map
        val overlay = Overlay(mapController.mapController)

        val point = GeoPoint(feedItem.address.lat, feedItem.address.lon)
        val icon = ContextCompat.getDrawable(context!!, R.drawable.ic_place_black_24dp)
        // Create an object for the layer
        val item = OverlayItem(point, icon)
        // Create the balloon model for the object
        val baloon = BalloonItem(context, item.geoPoint)
        baloon.text = feedItem.name
        // baloon.setOnBalloonListener(context)
        // Add the balloon model to the object
        item.balloonItem = baloon
        // Add the object to the layer
        overlay.addOverlayItem(item)
        mapController.setPositionAnimationTo(point, MapControllerWrapper.OPTIMAL_ZOOM)

        // Add the layer to the map
        mapController.overlayManager.addOverlay(overlay)
    }

    private fun setupWorkers() {
        val spaceBetween = resources.getDimension(R.dimen.master_space_between_workers).toInt();

        val size = resources.getDimension(R.dimen.master_worker_photo_size).toInt()
        val params = LinearLayout.LayoutParams(size, size)
        params.setMargins(spaceBetween, 0, spaceBetween, 0)

        val first = CircleImageView(context)
        first.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.all))
        first.layoutParams = params
        first.borderColor = ContextCompat.getColor(context!!, R.color.black)
        first.borderWidth = resources.getDimension(R.dimen.master_worker_border_width).toInt()


        val padding = resources.getDimension(R.dimen.master_workers_container_padding_vertical).toInt()
        binding.workersContainer.setPadding(0, padding, 0, padding)
        binding.workersContainer.addView(first)
        for (i in 0..10) {
            val worker = LinearLayout(context)
            val wrapperParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            wrapperParams.setMargins(0, 0, spaceBetween, 0)
            worker.layoutParams = wrapperParams
            worker.orientation = LinearLayout.VERTICAL


            val workerImage = CircleImageView(context)
            workerImage.layoutParams = LinearLayout.LayoutParams(size, size)

            workerImage.borderColor = ContextCompat.getColor(context!!, R.color.black)
            workerImage.borderWidth = resources.getDimension(R.dimen.master_worker_border_width).toInt()
            Glide
                    .with(context)
                    .load(photoUrls[i % photoUrls.size])
                    .into(workerImage)
            worker.addView(workerImage)

            val workerName = TextView(context)
            workerName.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_size_caption))
            workerName.text = names[Random().nextInt(names.size)]
            workerName.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            workerName.gravity = Gravity.CENTER
            worker.addView(workerName)

            binding.workersContainer.addView(worker)
        }
    }

    private fun setupSigns() {
        val requestBuilder = SvgModule.getGlideSvgRequestBuilder(context!!)
        for (sign in feedItem.signs) {
            val image = ImageView(context)
            val signSize = context!!.resources.getDimension(R.dimen.feed_signs_size).toInt()
            val params = LinearLayout.LayoutParams(signSize, signSize)
            params.setMargins(0, 0, context!!.resources.getDimension(R.dimen.master_space_between_signs).toInt(), 0)
            image.layoutParams = params
            binding.signsContainer.addView(image)
            requestBuilder?.load(Uri.parse(sign.photo.path))?.into(image);
        }
    }

    private fun setupOther() {
        binding.firstOpinionTv.text = "Vika: Была у Екатерины, хорошо сделала шеллак."
        binding.seeAllOpinionsTv.text = "Посмотреть все комментарии (42)"
        binding.initialCostTv.text = "Начальная стоимость от 400р"
        binding.addressTv.text = feedItem.address.description
    }

    private fun setupViewPager() {
        binding.pager.adapter = ViewPagerAdapter(context!!, feedItem.photos)
        val totalPages = feedItem.photos.size
        addBottomDots(binding.dotsContainer, 0, totalPages)
        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(pagePosition: Int) {
                addBottomDots(binding.dotsContainer, pagePosition, totalPages)
            }
        })
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

    companion object {
        fun getInstance(item: FeedItem): MasterFragment {
            val bundle = Bundle()
            bundle.putParcelable(FEED_ITEM_KEY, item)
            val fragment = MasterFragment()
            fragment.arguments = bundle
            return fragment
        }

        private const val FEED_ITEM_KEY = "FEED_ITEM_KEY"
    }
}