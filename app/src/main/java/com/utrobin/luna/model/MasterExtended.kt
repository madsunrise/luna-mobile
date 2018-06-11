package com.utrobin.luna.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MasterExtended(
        val base: MasterBase,
        val services: ArrayList<Service> = ArrayList()
) : Parcelable