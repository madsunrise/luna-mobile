package com.utrobin.luna.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressMetro(
        val station: String,
        val line: String,
        val color: String,
        val distance: Float
) : Parcelable