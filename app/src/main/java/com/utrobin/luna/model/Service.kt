package com.utrobin.luna.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Класс представляет собой всю услугу полностью (маникюр) и включает основные опции (взаимозаключащие) и дополнительные (по желанию)
 */
@Parcelize
data class Service(
        val type: ServiceType,
        val mainOptions: List<ServiceOption> = ArrayList(),     // Можно выбрать только одну из основных услуг
        val additionalOptions: List<ServiceOption> = ArrayList()
) : Parcelable {

    companion object {
        enum class ServiceType constructor(val value: String) {

            MANICURE("Маникюр"),
            PEDICURE("Педикюр");

            override fun toString() = this.value

            companion object {
                fun getEnum(value: String) =
                        values().first { it.value.equals(value, ignoreCase = true) }
            }
        }
    }
}