package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ivan on 02.01.2018.
 */

data class Address(val description: String, val lat: Double, val lon: Double) : Parcelable {

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(description)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble()
    )

    companion object {
        val CREATOR = object : Parcelable.Creator<Address> {
            override fun createFromParcel(`in`: Parcel): Address {
                return Address(`in`)
            }

            override fun newArray(size: Int): Array<Address?> {
                return arrayOfNulls(size)
            }
        }
    }
}