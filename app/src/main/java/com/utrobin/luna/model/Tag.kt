package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.FullPhoto
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tag(
        val id: Long,
        val name: String
) : Parcelable {
    constructor(tag: FullPhoto.Tag) : this(tag.id().toLong(), tag.name())
}