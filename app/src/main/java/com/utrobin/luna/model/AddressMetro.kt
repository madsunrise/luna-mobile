package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ivan on 04.03.2018.
 */

data class AddressMetro(val station: String, val line: String, val color: String, val distance: Float) : Parcelable {

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(station)
        parcel.writeString(line)
        parcel.writeString(color)
        parcel.writeFloat(distance)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readFloat()
    )

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<AddressMetro> {
            override fun createFromParcel(`in`: Parcel): AddressMetro {
                return AddressMetro(`in`)
            }

            override fun newArray(size: Int): Array<AddressMetro?> {
                return arrayOfNulls(size)
            }
        }
    }
}