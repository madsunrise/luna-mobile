package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ivan on 04.03.2018.
 */

data class AddressMetro(val color: String, val distance: Float, val line: String, val name: String) : Parcelable {

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(color)
        parcel.writeFloat(distance)
        parcel.writeString(line)
        parcel.writeString(name)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readFloat(),
            parcel.readString(),
            parcel.readString()
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