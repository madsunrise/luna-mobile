package com.utrobin.luna.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ivan on 29.10.2017.
 */

data class FeedItem(val name: String,
                    val avatar: Photo,
                    val address: Address,
                    val stars: Double,
                    val signs: List<Sign> = ArrayList(),
                    val photos: List<Photo> = ArrayList()
) : Parcelable {

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
        val CREATOR = object : Parcelable.Creator<FeedItem> {
            override fun createFromParcel(`in`: Parcel): FeedItem {
                return FeedItem(`in`)
            }

            override fun newArray(size: Int): Array<FeedItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}