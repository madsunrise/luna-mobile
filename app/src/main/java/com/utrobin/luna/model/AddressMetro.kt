package com.utrobin.luna.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by ivan on 04.03.2018.
 */
@Parcelize
data class AddressMetro(
        val station: String,
        val line: String,
        val color: String,
        val distance: Float
) : Parcelable