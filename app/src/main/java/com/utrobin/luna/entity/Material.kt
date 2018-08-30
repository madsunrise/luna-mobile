package com.utrobin.luna.entity

import android.os.Parcelable
import com.utrobin.luna.fragment.FullService
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Material(
        val id: Long,
        val firm: String,
        val description: String
) : Parcelable {

    constructor(material: FullService.Material) : this(
            id = material.id().toLong(),
            firm = material.firm(),
            description = material.description()
    )
}