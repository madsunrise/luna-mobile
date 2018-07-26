package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.AdditionalMaster
import com.utrobin.luna.fragment.FullReview
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Текущие записи к мастеру
 */
@Parcelize
data class Seance(
        val id: Long,
        val startTime: Date,
        val endTime: Date
) : Parcelable {

    constructor(seance: AdditionalMaster.Seance) : this(
            id = seance.fragments().fullSeance().id().toLong(),
            startTime = Date(seance.fragments().fullSeance().startTime().toLong()),
            endTime = Date(seance.fragments().fullSeance().endTime().toLong())
    )

    constructor(seance: AdditionalMaster.Seance1) : this(
            id = seance.fragments().fullSeance().id().toLong(),
            startTime = Date(seance.fragments().fullSeance().startTime().toLong()),
            endTime = Date(seance.fragments().fullSeance().endTime().toLong())
    )

    constructor(seance: FullReview.Seance) : this(
            id = seance.fragments().fullSeance().id().toLong(),
            startTime = Date(seance.fragments().fullSeance().startTime().toLong()),
            endTime = Date(seance.fragments().fullSeance().endTime().toLong())
    )
}