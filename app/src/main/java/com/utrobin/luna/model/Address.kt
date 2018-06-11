package com.utrobin.luna.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by ivan on 02.01.2018.
 */
@Parcelize
data class Address(
        val description: String,
        val lat: Double,
        val lon: Double,
        val metro: ArrayList<AddressMetro> = ArrayList()
) : Parcelable