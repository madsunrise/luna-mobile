package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ivan on 29.10.2017.
 */

data class Master(val name: String,
                  val avatar: Photo,
                  val address: Address,
                  val stars: Double,
                  val signs: List<Sign> = ArrayList(),
                  val photos: List<Photo> = ArrayList(),
                  var isFavorite: Boolean = false,
                  val services: ArrayList<Service> = ArrayList()
) : Parcelable {

    init {
        val manicure = Service(Service.Companion.ServiceType.MANICURE,
                ArrayList<ServiceOption>().apply {
                    add(ServiceOption("Обрезной / классический", 0))
                    add(ServiceOption("Аппаратный", 20000))
                    add(ServiceOption("Комбинированный", 15000))
                    add(ServiceOption("Европейский", 15000))
                },
                ArrayList<ServiceOption>().apply {
                    add(ServiceOption("Шеллак", 40000))
                    add(ServiceOption("Лечебные ванночки", 20000))
                    add(ServiceOption("Массаж рук", 10000))
                })
        val pedicure = Service(Service.Companion.ServiceType.PEDICURE,
                ArrayList<ServiceOption>().apply {
                    add(ServiceOption("Обрезной / классический", 0))
                    add(ServiceOption("Комбинированный", 20000))
                    add(ServiceOption("SPA-педикюр", 35000))
                },
                ArrayList<ServiceOption>().apply {
                    add(ServiceOption("Массаж ног", 25000))
                    add(ServiceOption("Скраб для ног", 20000))
                })
        services.add(manicure)
        services.add(pedicure)
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(name)
        parcel.writeParcelable(avatar, 0)
        parcel.writeParcelable(address, 0)
        parcel.writeDouble(stars)
        parcel.writeTypedList(signs)
        parcel.writeTypedList(photos)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable<Photo>(Photo::class.java.classLoader),
            parcel.readParcelable<Address>(Address::class.java.classLoader),
            parcel.readDouble()
    ) {
        parcel.readTypedList(signs, Sign.CREATOR)
        parcel.readTypedList(photos, Photo.CREATOR)
    }

    companion object {
        val CREATOR = object : Parcelable.Creator<Master> {
            override fun createFromParcel(`in`: Parcel): Master {
                return Master(`in`)
            }

            override fun newArray(size: Int): Array<Master?> {
                return arrayOfNulls(size)
            }
        }
    }
}