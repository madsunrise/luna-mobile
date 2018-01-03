package com.utrobin.luna.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

import com.bumptech.glide.Glide
import com.utrobin.luna.R
import com.utrobin.luna.model.Photo

/**
 * Created by ivan on 03.01.2018.
 */

internal class ViewPagerAdapter(context: Context, private val photos: List<Photo>) : PagerAdapter() {
    private val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getCount(): Int {
        return photos.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.photo_slider_item, container, false)

        val imageView = itemView.findViewById<ImageView>(R.id.image)
        Glide.with(imageView).load(photos[position].path).into(imageView)
        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}