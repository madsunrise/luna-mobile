package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.AdditionalMaster
import com.utrobin.luna.fragment.FullFeedItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
        val description: String,
        val lat: Double,
        val lon: Double,
        val metro: ArrayList<AddressMetro> = ArrayList()
) : Parcelable {

    constructor(address: FullFeedItem.Address) : this(
            address.fragments().fullAddress().description(),
            address.fragments().fullAddress().lat(),
            address.fragments().fullAddress().lon(),
            ArrayList()
    ) {
        this.metro.addAll(address.fragments().fullAddress().metros().map {
            AddressMetro(it)
        })
    }

    constructor(address: AdditionalMaster.Address) : this(
            address.fragments().fullAddress().description(),
            address.fragments().fullAddress().lat(),
            address.fragments().fullAddress().lon(),
            ArrayList()
    ) {
        this.metro.addAll(address.fragments().fullAddress().metros().map {
            AddressMetro(it)
        })
    }

    constructor(address: AdditionalMaster.Address1) : this(
            address.fragments().fullAddress().description(),
            address.fragments().fullAddress().lat(),
            address.fragments().fullAddress().lon(),
            ArrayList()
    ) {
        this.metro.addAll(address.fragments().fullAddress().metros().map {
            AddressMetro(it)
        })
    }
}