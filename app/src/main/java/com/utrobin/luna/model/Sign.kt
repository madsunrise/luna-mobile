package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sign(val name: String, val description: String, val icon: String) : Parcelable {
    constructor(sign: FeedQuery.Sign) : this(sign.name(), sign.description(), sign.icon())
    constructor(sign: MasterQuery.Sign) : this(sign.name(), sign.description(), sign.icon())
    constructor(sign: MasterQuery.Sign1) : this(sign.name(), sign.description(), sign.icon())
}