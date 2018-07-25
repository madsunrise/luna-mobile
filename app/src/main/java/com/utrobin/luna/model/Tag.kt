package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.MasterQuery
import com.utrobin.luna.fragment.PhotoInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tag(
        val id: Long,
        val name: String
) : Parcelable {
    constructor(tag: PhotoInfo.Tag) : this(tag.id().toLong(), tag.name())
    constructor(tag: MasterQuery.Tag) : this(tag.id().toLong(), tag.name())
    constructor(tag: MasterQuery.Tag1) : this(tag.id().toLong(), tag.name())
    constructor(tag: MasterQuery.Tag2) : this(tag.id().toLong(), tag.name())
    constructor(tag: MasterQuery.Tag3) : this(tag.id().toLong(), tag.name())
    constructor(tag: MasterQuery.Tag4) : this(tag.id().toLong(), tag.name())
    constructor(tag: MasterQuery.Tag5) : this(tag.id().toLong(), tag.name())
    constructor(tag: MasterQuery.Tag6) : this(tag.id().toLong(), tag.name())
}