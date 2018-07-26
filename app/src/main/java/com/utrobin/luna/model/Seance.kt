package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.MasterQuery
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

    constructor(seance: MasterQuery.Seance) : this(
            id = seance.fragments().fullSeance().id().toLong(),
            startTime = Date(seance.fragments().fullSeance().startTime().toLong()),
            endTime = Date(seance.fragments().fullSeance().endTime().toLong())
    )

    constructor(seance: MasterQuery.Seance1) : this(
            id = seance.fragments().fullSeance().id().toLong(),
            startTime = Date(seance.fragments().fullSeance().startTime().toLong()),
            endTime = Date(seance.fragments().fullSeance().endTime().toLong())
    )

    constructor(seance: MasterQuery.Seance2) : this(
            id = seance.fragments().fullSeance().id().toLong(),
            startTime = Date(seance.fragments().fullSeance().startTime().toLong()),
            endTime = Date(seance.fragments().fullSeance().endTime().toLong())
    )

    constructor(seance: MasterQuery.Seance3) : this(
            id = seance.fragments().fullSeance().id().toLong(),
            startTime = Date(seance.fragments().fullSeance().startTime().toLong()),
            endTime = Date(seance.fragments().fullSeance().endTime().toLong())
    )

    constructor(seance: MasterQuery.Seance4) : this(
            id = seance.fragments().fullSeance().id().toLong(),
            startTime = Date(seance.fragments().fullSeance().startTime().toLong()),
            endTime = Date(seance.fragments().fullSeance().endTime().toLong())
    )
}