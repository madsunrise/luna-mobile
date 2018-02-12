package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ivan on 29.10.2017.
 */

data class Master(
        val id: Long,
        val name: String,
        val avatar: Photo,
        val address: Address?,
        val stars: Double,
        val signs: List<Sign> = ArrayList(),
        val photos: List<Photo> = ArrayList(),
        val services: ArrayList<Service> = ArrayList()
) : Parcelable {

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeParcelable(avatar, 0)
        parcel.writeParcelable(address, 0)
        parcel.writeDouble(stars)
        parcel.writeTypedList(signs)
        parcel.writeTypedList(photos)
        parcel.writeTypedList(services)
    }

    override fun describeContents() = 0

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readParcelable<Photo>(Photo::class.java.classLoader),
            parcel.readParcelable<Address>(Address::class.java.classLoader),
            parcel.readDouble()
    ) {
        parcel.readTypedList(signs, Sign.CREATOR)
        parcel.readTypedList(photos, Photo.CREATOR)
        parcel.readTypedList(services, Service.CREATOR)
    }

    // Полная дичь
    constructor(item: FeedItem) : this(
            id = item.userId,
            name = item.name,
            avatar = item.avatar,
            photos = item.photos,
            stars = item.stars,
            signs = item.signs,
            address = null
    )

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