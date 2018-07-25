package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressMetro(
        val id: Long,
        val station: String,
        val line: String,
        val color: String,
        val distance: Float
) : Parcelable {
    constructor(addressMetro: FeedQuery.Metro) : this(
            addressMetro.id().toLong(),
            addressMetro.station(),
            addressMetro.line(),
            addressMetro.color(),
            addressMetro.distance().toFloat()
    )

    constructor(addressMetro: MasterQuery.Metro) : this(
            addressMetro.id().toLong(),
            addressMetro.station(),
            addressMetro.line(),
            addressMetro.color(),
            addressMetro.distance().toFloat()
    )

    constructor(addressMetro: MasterQuery.Metro1) : this(
            addressMetro.id().toLong(),
            addressMetro.station(),
            addressMetro.line(),
            addressMetro.color(),
            addressMetro.distance().toFloat()
    )
}