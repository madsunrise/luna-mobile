package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.AllFields
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tag(
        val id: Long,
        val name: String
) : Parcelable {
    constructor(tag: AllFields.Tag) : this(tag.id().toLong(), tag.name())
}