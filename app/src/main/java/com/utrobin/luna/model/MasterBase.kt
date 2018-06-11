package com.utrobin.luna.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by ivan on 29.10.2017.
 */
@Parcelize
data class MasterBase(
        val id: Long,
        val name: String?,
        val avatar: Photo?,
        val address: Address?,
        val stars: Double,
        val signs: List<Sign> = ArrayList(),
        val photos: List<Photo> = ArrayList()
) : Parcelable