package com.utrobin.luna.ui.view

import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter
import com.utrobin.luna.databinding.ImageViewerBinding
import com.utrobin.luna.model.Photo
import com.utrobin.luna.ui.view.MasterActivity.Companion.CURRENT_PHOTO
import com.utrobin.luna.ui.view.MasterActivity.Companion.TRANSITION_NAME
import com.utrobin.luna.utils.SwipeableLayout
import com.utrobin.luna.utils.listener.OnLayoutPercentageChangeListener

/**
 * Created by ivan on 04.03.2018.
 */

class ImageViewer : AppCompatActivity() {
    private lateinit var binding: ImageViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.image_viewer)


        val photos = intent.getParcelableArrayListExtra<Photo>(PHOTO_LIST_EXTRA)
        binding.imageSlider.adapter = ViewPagerAdapter(this, photos.toList())


        val swipeableLayout = findViewById<SwipeableLayout>(R.id.swipeableLayout)

        swipeableLayout.setOnLayoutPercentageChangeListener(object : OnLayoutPercentageChangeListener() {
            override fun percentageY(percentage: Float) {

            }
        })

        swipeableLayout.setLayoutShiftListener { positionX, positionY, isTouched ->
            //DO SOME YOUR LOGIC
        }

        swipeableLayout.setOnSwipedListener { finish() }

        supportPostponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.imageSlider.transitionName = intent.extras.getString(TRANSITION_NAME)
            binding.imageSlider.currentItem = intent.extras.getInt(CURRENT_PHOTO)
        }
        supportStartPostponedEnterTransition()
    }

    companion object {
        const val PHOTO_LIST_EXTRA = "photo_list_extra"
    }
}
