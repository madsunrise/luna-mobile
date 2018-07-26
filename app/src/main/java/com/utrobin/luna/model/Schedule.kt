package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.AdditionalMaster
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Расписание мастера
 */

@Parcelize
data class Schedule(
        val id: Long,
        val startTime: Date,
        val endTime: Date
) : Parcelable {

    constructor(schedule: AdditionalMaster.Schedule) : this(
            id = schedule.fragments().fullSchedule().id().toLong(),
            startTime = Date(schedule.fragments().fullSchedule().startTime().toLong()),
            endTime = Date(schedule.fragments().fullSchedule().endTime().toLong())
    )

    constructor(schedule: AdditionalMaster.Schedule1) : this(
            id = schedule.fragments().fullSchedule().id().toLong(),
            startTime = Date(schedule.fragments().fullSchedule().startTime().toLong()),
            endTime = Date(schedule.fragments().fullSchedule().endTime().toLong())
    )
}