package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.FeedQuery
import com.utrobin.luna.MasterQuery
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Material(
        val id: Long,
        val firm: String,
        val description: String
) : Parcelable {

    constructor(material: FeedQuery.Material) : this(
            material.id().toLong(),
            material.firm(),
            material.description()
    )

    constructor(material: MasterQuery.Material) : this(
            material.id().toLong(),
            material.firm(),
            material.description()
    )

    constructor(material: MasterQuery.Material1) : this(
            material.id().toLong(),
            material.firm(),
            material.description()
    )

    constructor(material: MasterQuery.Material2) : this(
            material.id().toLong(),
            material.firm(),
            material.description()
    )
}