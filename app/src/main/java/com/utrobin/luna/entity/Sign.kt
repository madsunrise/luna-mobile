package com.utrobin.luna.entity

import android.os.Parcelable
import com.utrobin.luna.fragment.FullFeedItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sign(val id: Long, val name: String, val description: String, val icon: String) : Parcelable {

    constructor(sign: FullFeedItem.Sign) : this(
            id = sign.fragments().fullSign().id().toLong(),
            name = sign.fragments().fullSign().name(),
            description = sign.fragments().fullSign().description(),
            icon = sign.fragments().fullSign().icon()
    )
}