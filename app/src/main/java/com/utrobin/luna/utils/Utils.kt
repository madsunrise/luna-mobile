package com.utrobin.luna.utils

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.widget.ImageView
import com.utrobin.luna.R
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    fun fillContainerWithStars(context: Context, rating: Double, container: ViewGroup, starParams: ViewGroup.LayoutParams) {

        val fullStarsCount = rating.toInt()
        val halfStarPresented = rating - fullStarsCount >= 0.5


        for (i in 0 until fullStarsCount) {
            val fullStar = ImageView(context)
            fullStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_black_24dp))
            fullStar.layoutParams = starParams
            container.addView(fullStar)
        }

        if (halfStarPresented) {
            val halfStar = ImageView(context)
            halfStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_half_black_24dp))
            halfStar.layoutParams = starParams
            container.addView(halfStar)
        }

        for (i in 4 downTo container.childCount) {
            val emptyStar = ImageView(context)
            emptyStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border_black_24dp))
            emptyStar.layoutParams = starParams
            container.addView(emptyStar)
        }
    }
}