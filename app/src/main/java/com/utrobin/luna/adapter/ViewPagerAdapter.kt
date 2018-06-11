package com.utrobin.luna.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.utrobin.luna.R
import com.utrobin.luna.model.Photo
import io.reactivex.subjects.PublishSubject

internal class ViewPagerAdapter(
        context: Context,
        private val photos: List<Photo>) : PagerAdapter() {
    private val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    val imageClickSubject: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    override fun getCount(): Int {
        return photos.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.photo_slider_item, container, false)

        val imageView = itemView.findViewById<ImageView>(R.id.image)
        Glide.with(imageView).load(photos[position].path).into(imageView)
        container.addView(itemView)

        imageView.setOnClickListener { imageClickSubject.onNext(true) }

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as FrameLayout)
    }
}