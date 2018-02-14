package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ivan on 07.01.2018.
 *
 * Класс представляет собой конкретную опцию с ценой (например, массаж рук)
 */

data class ServiceOption(val name: String, val price: Long) : Parcelable {

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(name)
        parcel.writeLong(price)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readLong()
    )

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ServiceOption> {
            override fun createFromParcel(`in`: Parcel): ServiceOption {
                return ServiceOption(`in`)
            }

            override fun newArray(size: Int): Array<ServiceOption?> {
                return arrayOfNulls(size)
            }
        }
    }
}