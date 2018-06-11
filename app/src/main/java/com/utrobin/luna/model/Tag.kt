package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tag(val name: String) : Parcelable {
    constructor(tag: FeedQuery.Tag) : this(tag.name())
    constructor(tag: FeedQuery.Tag1) : this(tag.name())
}