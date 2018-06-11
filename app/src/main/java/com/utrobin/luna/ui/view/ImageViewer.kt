package com.utrobin.luna.ui.view

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import com.utrobin.luna.R
import com.utrobin.luna.adapter.ViewPagerAdapter
import com.utrobin.luna.model.Photo
import com.utrobin.luna.ui.view.MasterActivity.Companion.CURRENT_PHOTO
import com.utrobin.luna.ui.view.MasterActivity.Companion.TRANSITION_NAME

class ImageViewer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_viewer)

        val photos = intent.getParcelableArrayListExtra<Photo>(PHOTO_LIST_EXTRA)
        imageSlider.adapter = ViewPagerAdapter(this, photos.toList())

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        imageSlider.layoutParams.height = displayMetrics.widthPixels * 9 / 16


        supportPostponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageSlider.transitionName = intent.extras.getString(TRANSITION_NAME)
            imageSlider.currentItem = intent.extras.getInt(CURRENT_PHOTO)
        }
        supportStartPostponedEnterTransition()
    }

    companion object {
        const val PHOTO_LIST_EXTRA = "photo_list_extra"
    }
}
