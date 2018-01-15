package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter
import com.utrobin.luna.adapter.ViewPagerAdapter.Companion.addBottomDots
import com.utrobin.luna.databinding.MasterFragmentBinding
import com.utrobin.luna.model.Master
import com.utrobin.luna.ui.contract.MasterContract
import com.utrobin.luna.ui.presenter.MasterPresenter
import com.utrobin.luna.utils.MapControllerWrapper
import com.utrobin.luna.utils.svg.SvgModule
import ru.yandex.yandexmapkit.overlay.Overlay
import ru.yandex.yandexmapkit.overlay.OverlayItem
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem
import ru.yandex.yandexmapkit.utils.GeoPoint
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by ivan on 01.11.2017.
 */
class MasterFragment : Fragment(), MasterContract.View {
    private lateinit var master: Master
    lateinit var binding: MasterFragmentBinding

    private val presenter = MasterPresenter()

    private var totalPrice by Delegates.observable(0L) { _, _, new ->
        binding.totalPrice.text = String.format(getString(R.string.total_price_x_rubles), new / 100)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = arguments?.getLong(USER_ID_EXTRA) ?: throw NullPointerException("No arguments provided!")
        // TODO Master loading
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.master_fragment, container, false)!!

        binding.toolbar.title = master.name
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true);
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true);

        setupMap()
        setupWorkers()
        setupSigns()
        setupViewPager()
        setupServices()
        setupOther()
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

        val point = GeoPoint(master.address.lat, master.address.lon)
        val icon = ContextCompat.getDrawable(context!!, R.drawable.ic_place_black_24dp)
        // Create an object for the layer
        val item = OverlayItem(point, icon)
        // Create the balloon model for the object
        val baloon = BalloonItem(context, item.geoPoint)
        baloon.text = master.name
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

        val allWorkers = ImageView(context)
        allWorkers.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.all))
        allWorkers.layoutParams = params

        Glide
                .with(context)
                .load(R.drawable.all)
                .apply(RequestOptions.circleCropTransform())
                .into(allWorkers)

        val padding = resources.getDimension(R.dimen.master_workers_container_padding_vertical).toInt()
        binding.workersContainer.setPadding(0, padding, 0, padding)
        binding.workersContainer.addView(allWorkers)
        for (i in 0..10) {
            val worker = LinearLayout(context)
            val wrapperParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            wrapperParams.setMargins(0, 0, spaceBetween, 0)
            worker.layoutParams = wrapperParams
            worker.orientation = LinearLayout.VERTICAL


            val workerImage = ImageView(context)
            workerImage.layoutParams = LinearLayout.LayoutParams(size, size)

            Glide
                    .with(context)
                    .load(photoUrls[i % photoUrls.size])
                    .apply(RequestOptions.circleCropTransform())
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
        for (sign in master.signs) {
            val image = ImageView(context)
            val signSize = context!!.resources.getDimension(R.dimen.feed_signs_size).toInt()
            val params = LinearLayout.LayoutParams(signSize, signSize)
            params.setMargins(0, 0, context!!.resources.getDimension(R.dimen.master_space_between_signs).toInt(), 0)
            image.layoutParams = params
            binding.signsContainer.addView(image)
            requestBuilder?.load(Uri.parse(sign.icon))?.into(image);
        }
    }


    private fun setupViewPager() {
        binding.included?.pager?.adapter = ViewPagerAdapter(context!!, master.photos)
        val totalPages = master.photos.size
        addBottomDots(binding.included!!.dotsContainer, 0, totalPages)
        binding.included?.pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(pagePosition: Int) {
                addBottomDots(binding.included!!.dotsContainer, pagePosition, totalPages)
            }
        })
        binding.included?.bookmark?.setOnClickListener { presenter.onBookmarkClicked() }
    }


    private fun setupServices() {
        totalPrice = 0L // Для инициализации TextView

        binding.servicesContainer.addView(getDivider(ContextCompat.getColor(context!!, R.color.dark_divider_color)))
        master.services.forEach { service ->

            val serviceContainer = LinearLayout(context)
            serviceContainer.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            serviceContainer.orientation = LinearLayout.VERTICAL
            serviceContainer.visibility = View.GONE

            val serviceEnablingSwitch = constructSwitch(service.type.value, CompoundButton.OnCheckedChangeListener { _, checked ->
                serviceContainer.visibility = if (checked) View.VISIBLE else View.GONE
            })


            binding.servicesContainer.addView(serviceEnablingSwitch)
            binding.servicesContainer.addView(serviceContainer)
            binding.servicesContainer.addView(getDivider(ContextCompat.getColor(context!!, R.color.dark_divider_color)))


            // Конструирование содержания услуги внутри serviceContainer

            val radioGroup = RadioGroup(context)
            radioGroup.orientation = RadioGroup.VERTICAL

            service.mainOptions.forEachIndexed { optionIndex, option ->
                val radio = RadioButton(context)
                radio.id = optionIndex
                radio.text = String.format(getString(R.string.service_option_with_price, option.name, option.price / 100))
                radioGroup.addView(radio)
            }

            var selectedRadio: Int? = null
            radioGroup.setOnCheckedChangeListener { _, radioId ->
                selectedRadio?.let {
                    totalPrice -= service.mainOptions[it].price
                }
                selectedRadio = radioId
                totalPrice += service.mainOptions[radioId].price
            }

            serviceContainer.addView(radioGroup)
            serviceContainer.addView(getDivider())


            val coverSwitch = constructSwitch(
                    getString(R.string.without_cover),
                    CompoundButton.OnCheckedChangeListener { view, checked ->
                        if (checked) {
                            view.text = getString(R.string.with_cover)
                        } else {
                            view.text = getString(R.string.without_cover)
                        }
                    }
            )

            serviceContainer.addView(coverSwitch)
            serviceContainer.addView(getDivider())


            service.additionalOptions.forEachIndexed { index, option ->
                val switch = constructSwitch(
                        String.format(getString(R.string.service_option_with_price, option.name, option.price / 100)),
                        CompoundButton.OnCheckedChangeListener { _, checked ->
                            if (checked) {
                                totalPrice += option.price
                            } else {
                                totalPrice -= option.price
                            }
                        }
                )
                serviceContainer.addView(switch)
                if (index != service.additionalOptions.size - 1) {
                    serviceContainer.addView(getDivider())
                }
            }
        }
    }


    private fun constructSwitch(text: String, listener: CompoundButton.OnCheckedChangeListener? = null): SwitchCompat {
        val switch = SwitchCompat(context)
        switch.text = text
        switch.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_size_body))
        val paddingVertical = resources.getDimension(R.dimen.master_switch_padding_vertical).toInt()
        val paddingHorizontal = resources.getDimension(R.dimen.master_side_elements_padding).toInt()
        switch.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
        switch.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        switch.setOnCheckedChangeListener(listener)
        return switch
    }

    private fun getDivider(color: Int = ContextCompat.getColor(context!!, R.color.divider_color)): View {
        val divider = View(context)
        divider.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                resources.getDimension(R.dimen.master_divider_height).toInt()
        )
        divider.setBackgroundColor(color)
        return divider
    }

    private fun setupOther() {
        binding.firstOpinionTv.text = "Vika: Была у Екатерины, хорошо сделала шеллак."
        binding.seeAllOpinionsTv.text = "Посмотреть все комментарии (42)"
        binding.addressTv.text = master.address.description
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        fun getInstance(userId: Long): MasterFragment {
            val bundle = Bundle()
            bundle.putLong(USER_ID_EXTRA, userId)
            val fragment = MasterFragment()
            fragment.arguments = bundle
            return fragment
        }

        private const val USER_ID_EXTRA = "USER_ID_EXTRA"
    }
}