package com.utrobin.luna.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Класс представляет собой конкретную опцию с ценой (например, массаж рук)
 */
@Parcelize
data class ServiceOption(val name: String, val price: Long) : Parcelable