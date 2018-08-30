package com.utrobin.luna.entity

import android.os.Parcelable
import com.utrobin.luna.fragment.FullService
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceType(
        val id: Long,
        val name: String,
        val parent: ServiceType?
) : Parcelable {

    constructor(type: FullService.Type) : this(
            id = type.id().toLong(),
            name = type.name(),
            parent = type.parent()?.let { ServiceType(it) }
    )

    constructor(type: FullService.Parent) : this(
            id = type.id().toLong(),
            name = type.name(),
            parent = null
    )
}