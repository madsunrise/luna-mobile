package com.utrobin.luna.utils

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.widget.ImageView
import com.utrobin.luna.R
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    fun fillContainerWithBigStars(context: Context, rating: Double, container: ViewGroup, starParams: ViewGroup.LayoutParams) {
        fillContainer(context, rating, container, starParams, R.drawable.ic_star_dark_24dp,
                R.drawable.ic_star_half_dark_24dp, R.drawable.ic_star_border_dark_24dp
        )
    }

    fun fillContainerWithSmallStars(context: Context, rating: Double, container: ViewGroup, starParams: ViewGroup.LayoutParams) {
        fillContainer(context, rating, container, starParams, R.drawable.ic_star_dark_12dp,
                R.drawable.ic_star_half_dark_12dp, R.drawable.ic_star_border_dark_12dp
        )
    }

    private fun fillContainer(
            context: Context,
            rating: Double,
            container: ViewGroup,
            starParams: ViewGroup.LayoutParams,
            @DrawableRes starResource: Int,
            @DrawableRes halfStarResource: Int,
            @DrawableRes borderStarResource: Int) {
        val fullStarsCount = rating.toInt()
        val halfStarPresented = rating - fullStarsCount >= 0.5


        for (i in 0 until fullStarsCount) {
            val fullStar = ImageView(context)
            fullStar.setImageDrawable(ContextCompat.getDrawable(context, starResource))
            fullStar.layoutParams = starParams
            container.addView(fullStar)
        }

        if (halfStarPresented) {
            val halfStar = ImageView(context)
            halfStar.setImageDrawable(ContextCompat.getDrawable(context, halfStarResource))
            halfStar.layoutParams = starParams
            container.addView(halfStar)
        }

        for (i in 4 downTo container.childCount) {
            val emptyStar = ImageView(context)
            emptyStar.setImageDrawable(ContextCompat.getDrawable(context, borderStarResource))
            emptyStar.layoutParams = starParams
            container.addView(emptyStar)
        }
    }
}