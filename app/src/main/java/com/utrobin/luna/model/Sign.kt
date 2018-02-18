package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable
import com.utrobin.luna.FeedQuery

/**
 * Created by ivan on 02.01.2018.
 */

data class Sign(val name: String, val description: String, val icon: String) : Parcelable {
    constructor(sign: FeedQuery.Sign) : this(sign.name()!!, sign.description()!!, sign.icon()!!)

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(icon)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Sign> {
            override fun createFromParcel(`in`: Parcel): Sign {
                return Sign(`in`)
            }

            override fun newArray(size: Int): Array<Sign?> {
                return arrayOfNulls(size)
            }
        }
    }
}