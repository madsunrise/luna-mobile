package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ivan on 07.01.2018.
 *
 * Класс представляет собой всю услугу полностью (маникюр) и включает основные опции (взаимозаключащие) и дополнительные (по желанию)
 */

data class Service(
        val type: ServiceType,
        val mainOptions: List<ServiceOption> = ArrayList(),     // Можно выбрать только одну из основных услуг
        val additionalOptions: List<ServiceOption> = ArrayList()
) : Parcelable {

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(type.value)
        parcel.writeTypedList(mainOptions)
        parcel.writeTypedList(additionalOptions)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(ServiceType.getEnum(parcel.readString())) {
        parcel.readTypedList(mainOptions, ServiceOption.CREATOR)
        parcel.readTypedList(additionalOptions, ServiceOption.CREATOR)
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Service> {
            override fun createFromParcel(`in`: Parcel): Service {
                return Service(`in`)
            }

            override fun newArray(size: Int): Array<Service?> {
                return arrayOfNulls(size)
            }
        }


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