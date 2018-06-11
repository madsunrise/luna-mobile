package com.utrobin.luna.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by ivan on 13.02.2018.
 */
@Parcelize
data class MasterExtended(
        val base: MasterBase,
        val services: ArrayList<Service> = ArrayList()
) : Parcelable