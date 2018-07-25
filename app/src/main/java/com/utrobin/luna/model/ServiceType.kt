package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceType(
        val id: Long,
        val name: String,
        val parent: ServiceType?
) : Parcelable {

    // Ну это ненормально -_-

    constructor(type: FeedQuery.Parent) : this(
            type.id().toLong(),
            type.name(),
            null
    )

    constructor(type: MasterQuery.Parent) : this(
            type.id().toLong(),
            type.name(),
            null
    )

    constructor(type: MasterQuery.Parent1) : this(
            type.id().toLong(),
            type.name(),
            null
    )

    constructor(type: MasterQuery.Parent2) : this(
            type.id().toLong(),
            type.name(),
            null
    )

    constructor(type: FeedQuery.Type) : this(
            type.id().toLong(),
            type.name(),
            type.parent()?.let { ServiceType(it) }
    )

    constructor(type: MasterQuery.Type) : this(
            type.id().toLong(),
            type.name(),
            type.parent()?.let { ServiceType(it) }
    )

    constructor(type: MasterQuery.Type1) : this(
            type.id().toLong(),
            type.name(),
            type.parent()?.let { ServiceType(it) }
    )

    constructor(type: MasterQuery.Type2) : this(
            type.id().toLong(),
            type.name(),
            type.parent()?.let { ServiceType(it) }
    )
}