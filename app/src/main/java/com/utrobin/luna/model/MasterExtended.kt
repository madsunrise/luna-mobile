package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ivan on 13.02.2018.
 */


data class MasterExtended(
        val base: MasterBase,
        val services: ArrayList<Service> = ArrayList()
) : Parcelable {

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeParcelable(base, 0)
        parcel.writeTypedList(services)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(
            parcel.readParcelable<MasterBase>(MasterBase::class.java.classLoader)
    ) {
        parcel.readTypedList(services, Service.CREATOR)
    }


    companion object {
        val CREATOR = object : Parcelable.Creator<MasterExtended> {
            override fun createFromParcel(`in`: Parcel): MasterExtended {
                return MasterExtended(`in`)
            }

            override fun newArray(size: Int): Array<MasterExtended?> {
                return arrayOfNulls(size)
            }
        }
    }
}