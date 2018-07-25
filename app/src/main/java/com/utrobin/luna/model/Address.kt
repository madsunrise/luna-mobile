package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
        val description: String,
        val lat: Double,
        val lon: Double,
        val metro: ArrayList<AddressMetro> = ArrayList()
) : Parcelable {
    constructor(address: FeedQuery.Address) : this(
            address.description(),
            address.lat(),
            address.lon(),
            ArrayList()
    ) {
        this.metro.addAll(address.metros().map {
            AddressMetro(it)
        })
    }

    constructor(address: MasterQuery.Address) : this(
            address.description(),
            address.lat(),
            address.lon(),
            ArrayList()
    ) {
        this.metro.addAll(address.metros().map {
            AddressMetro(it)
        })
    }

    constructor(address: MasterQuery.Address1) : this(
            address.description(),
            address.lat(),
            address.lon(),
            ArrayList()
    ) {
        this.metro.addAll(address.metros().map {
            AddressMetro(it)
        })
    }
}