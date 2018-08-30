package com.utrobin.luna.entity

import android.os.Parcelable
import com.utrobin.luna.fragment.FullAddress
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressMetro(
        val id: Long,
        val station: String,
        val line: String,
        val color: String,
        val distance: Float
) : Parcelable {
    constructor(addressMetro: FullAddress.Metro) : this(
            id = addressMetro.id().toLong(),
            station = addressMetro.station(),
            line = addressMetro.line(),
            color = addressMetro.color(),
            distance = addressMetro.distance().toFloat()
    )
}