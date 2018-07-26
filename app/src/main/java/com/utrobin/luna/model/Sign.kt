package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sign(val id: Long, val name: String, val description: String, val icon: String) : Parcelable {

    constructor(sign: FeedQuery.Sign) : this(
            id = sign.fragments().fullSign().id().toLong(),
            name = sign.fragments().fullSign().name(),
            description = sign.fragments().fullSign().description(),
            icon = sign.fragments().fullSign().icon()
    )

    constructor(sign: MasterQuery.Sign) : this(
            id = sign.fragments().fullSign().id().toLong(),
            name = sign.fragments().fullSign().name(),
            description = sign.fragments().fullSign().description(),
            icon = sign.fragments().fullSign().icon()
    )

    constructor(sign: MasterQuery.Sign1) : this(
            id = sign.fragments().fullSign().id().toLong(),
            name = sign.fragments().fullSign().name(),
            description = sign.fragments().fullSign().description(),
            icon = sign.fragments().fullSign().icon()
    )
}