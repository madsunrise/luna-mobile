package com.utrobin.luna.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
        val description: String,
        val lat: Double,
        val lon: Double,
        val metro: ArrayList<AddressMetro> = ArrayList()
) : Parcelable