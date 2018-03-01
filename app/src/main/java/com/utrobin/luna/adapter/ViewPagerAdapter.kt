package com.utrobin.luna.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.text.Html
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.utrobin.luna.R
import com.utrobin.luna.model.Photo
import io.reactivex.subjects.PublishSubject
import java.util.*

/**
 * Created by ivan on 03.01.2018.
 */

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

    companion object {
        fun addBottomDots(dotsContainer: LinearLayout, currentPage: Int, totalPages: Int) {
            dotsContainer.removeAllViews()
            if (totalPages < 2) {
                return
            }
            val dots = ArrayList<TextView>()
            for (i in 0 until totalPages) {
                val textView = TextView(dotsContainer.context)
                textView.text =
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY);
                        } else {
                            Html.fromHtml("&#8226;")
                        }

                val color = if (i != currentPage) {
                    textView.setTextSize(COMPLEX_UNIT_SP, dotsContainer.context.resources.getDimension(
                            R.dimen.feed_image_slider_inactive_dot_size))
                    ContextCompat.getColor(dotsContainer.context, R.color.viewPagerInactiveDot)
                } else {
                    textView.setTextSize(COMPLEX_UNIT_SP, dotsContainer.context.resources.getDimension(
                            R.dimen.feed_image_slider_active_dot_size))
                    ContextCompat.getColor(dotsContainer.context, R.color.viewPagerActiveDot)
                }
                textView.setTextColor(color)

                dotsContainer.addView(textView)
                dots.add(textView)
            }
        }
    }
}