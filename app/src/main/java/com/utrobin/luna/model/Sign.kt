package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import kotlinx.android.parcel.Parcelize

/**
 * Created by ivan on 02.01.2018.
 */

@Parcelize
data class Sign(val name: String, val description: String, val icon: String) : Parcelable {
    constructor(sign: FeedQuery.Sign) : this(sign.name(), sign.description(), sign.icon())
}