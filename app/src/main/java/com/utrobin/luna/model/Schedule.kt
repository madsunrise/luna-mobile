package com.utrobin.luna.model

import android.os.Parcelable
import com.utrobin.luna.fragment.AdditionalMaster
import com.utrobin.luna.utils.Utils.dateFormatter
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
            startTime = dateFormatter.parse(schedule.fragments().fullSchedule().startTime()),
            endTime = dateFormatter.parse(schedule.fragments().fullSchedule().endTime())
    )
}